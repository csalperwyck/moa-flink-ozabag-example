/*
 * Copyright 2018 - Christophe Salperwyck
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.  
 */
package moa.flink.ozabag;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import com.yahoo.labs.samoa.instances.Instance;

import moa.classifiers.trees.HoeffdingTree;
import moa.core.Example;
import moa.core.MiscUtils;

public class LearningFunction extends ProcessFunction<Example<Instance>, String> {

	private static final long serialVersionUID = 1L;
	private HoeffdingTree ht;
	private long examplesSeen = 0L;
	private Random random;
	private static AtomicInteger seq = new AtomicInteger(0);
	private long seed;
	private boolean timerOn = false;
	private long savingInterval;
	
	public LearningFunction(long savingInterval) {
		this.savingInterval = savingInterval;
	}

	@Override
	public void open(Configuration parameters) {
		seed = seq.getAndIncrement();
		random = new Random(seed);
		ht = new HoeffdingTree();
		ht.prepareForUse();
	}

	@Override
	public void processElement(Example<Instance> record, ProcessFunction<Example<Instance>, String>.Context ctx, Collector<String> collector) {
		
		examplesSeen++;
        int k = MiscUtils.poisson(1.0, random);
        // train
        if (k > 0) {
        	try {
        		record.setWeight(record.weight() * k);
	            ht.trainOnInstance(record);
        	} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        // register an event to save the model
        if (!timerOn) {
        	TimerService timerService = ctx.timerService();
        	timerService.registerEventTimeTimer(timerService.currentProcessingTime() + savingInterval);
        	timerOn = true;
        }
		
        // for logging purpose
		if (examplesSeen % 500_000 == 0) {
			collector.collect("HT with OzaBag seed " + seed + " already processed " + examplesSeen + " examples");
		}
	}
	
	@Override
	public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> collector) throws Exception {
		
		// just serialize as text the model so that we can have a look at what we've got
		StringBuilder sb = new StringBuilder();
		synchronized (ht) {
			ht.getModelDescription(sb, 2);
		}
		
		collector.collect(
				// save the model in a file, but we could push to our online system!
				Files.write(
					Paths.get("model/FlinkMOA_" + timestamp + "_seed-" + seed + ".model"), 
					sb.toString().getBytes(), 
					StandardOpenOption.CREATE_NEW).toString());
		
		// we can trigger the next event now
		timerOn = false;
	}

}
