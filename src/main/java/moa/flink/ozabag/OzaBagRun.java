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

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamContextEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.yahoo.labs.samoa.instances.Instance;

import moa.core.Example;

public class OzaBagRun {
	
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamContextEnvironment.createLocalEnvironment(10);
		env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
		
		// create a source of data from the RRBF generator
		DataStreamSource<Example<Instance>> rrbfSource = env.addSource(new RRBFSource());
		BaggingKeySelector selector = new BaggingKeySelector();
		
		rrbfSource.keyBy(selector). process(new LearningFunction(60_000L)).print();
		
		//want to see your execution plan :-)
		//http://flink.apache.org/visualizer/index.html
		//System.out.println(env.getExecutionPlan());
		
		// fire execution
		env.execute("MOA with Flink");
	}

}
