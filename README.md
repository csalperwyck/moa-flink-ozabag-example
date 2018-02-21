# Streaming Machine Learning with Flink and MOA - Online bagging (OzaBag)#
This project combines:
- Flink as a stream engine: https://flink.apache.org/
- MOA as stream machine learning library: https://moa.cms.waikato.ac.nz/

## Data ##
The data is generated using the MOA RandomRBF generator.
Many generators with the same seed will run in parallel so that each classifier will receive the same examples. It is in each classifier that we will decide which weight to put on each instance.


## Model update ##
The model is updated every minute.
In our case we save the model on disk, but it could be pushed into a web service or some other serving layer.


## Output ##
TODO: assess performance. It might be a bit tricky here as we need to collect all classifiers and bag them, but we need to know which one to replace in the bag when there is an update.
In the meantime you can use MOA to check the performances.

```
Connected to JobManager at Actor[akka://flink/user/jobmanager_1#-425324055] with leader session id 5f617fa1-7c02-4288-b305-7d97413ee2d8.
02/21/2018 16:51:05	Job execution switched to status RUNNING.
02/21/2018 16:51:05	Source: Random RBF Source(1/4) switched to SCHEDULED 
02/21/2018 16:51:05	Source: Random RBF Source(2/4) switched to SCHEDULED 
02/21/2018 16:51:05	Source: Random RBF Source(3/4) switched to SCHEDULED 
02/21/2018 16:51:05	Source: Random RBF Source(4/4) switched to SCHEDULED 
02/21/2018 16:51:05	Process -> Sink: Unnamed(1/4) switched to SCHEDULED 
02/21/2018 16:51:05	Process -> Sink: Unnamed(2/4) switched to SCHEDULED 
02/21/2018 16:51:05	Process -> Sink: Unnamed(3/4) switched to SCHEDULED 
02/21/2018 16:51:05	Process -> Sink: Unnamed(4/4) switched to SCHEDULED 
02/21/2018 16:51:05	Source: Random RBF Source(1/4) switched to DEPLOYING 
02/21/2018 16:51:05	Source: Random RBF Source(2/4) switched to DEPLOYING 
02/21/2018 16:51:05	Source: Random RBF Source(3/4) switched to DEPLOYING 
02/21/2018 16:51:05	Source: Random RBF Source(4/4) switched to DEPLOYING 
02/21/2018 16:51:05	Process -> Sink: Unnamed(1/4) switched to DEPLOYING 
02/21/2018 16:51:05	Process -> Sink: Unnamed(2/4) switched to DEPLOYING 
02/21/2018 16:51:05	Process -> Sink: Unnamed(3/4) switched to DEPLOYING 
02/21/2018 16:51:05	Process -> Sink: Unnamed(4/4) switched to DEPLOYING 
02/21/2018 16:51:05	Process -> Sink: Unnamed(3/4) switched to RUNNING 
02/21/2018 16:51:05	Source: Random RBF Source(1/4) switched to RUNNING 
02/21/2018 16:51:05	Process -> Sink: Unnamed(4/4) switched to RUNNING 
02/21/2018 16:51:05	Source: Random RBF Source(3/4) switched to RUNNING 
02/21/2018 16:51:05	Process -> Sink: Unnamed(2/4) switched to RUNNING 
02/21/2018 16:51:05	Source: Random RBF Source(2/4) switched to RUNNING 
02/21/2018 16:51:05	Source: Random RBF Source(4/4) switched to RUNNING 
02/21/2018 16:51:05	Process -> Sink: Unnamed(1/4) switched to RUNNING 
1> HT with OzaBag seed 3 already processed 500000 examples
3> HT with OzaBag seed 2 already processed 500000 examples
2> HT with OzaBag seed 0 already processed 500000 examples
4> HT with OzaBag seed 1 already processed 500000 examples
Can not access instrumentation environment.
Please check if jar file containing SizeOfAgent class is 
specified in the java's "-javaagent" command line argument.
1> HT with OzaBag seed 3 already processed 1000000 examples
3> HT with OzaBag seed 2 already processed 1000000 examples
2> HT with OzaBag seed 0 already processed 1000000 examples
4> HT with OzaBag seed 1 already processed 1000000 examples
1> HT with OzaBag seed 3 already processed 1500000 examples
3> HT with OzaBag seed 2 already processed 1500000 examples
2> HT with OzaBag seed 0 already processed 1500000 examples
4> HT with OzaBag seed 1 already processed 1500000 examples
1> HT with OzaBag seed 3 already processed 2000000 examples
3> HT with OzaBag seed 2 already processed 2000000 examples
2> Saving model on disk, file model\FlinkMOA_1519228325596_seed-0.model
4> Saving model on disk, file model\FlinkMOA_1519228325596_seed-1.model
1> Saving model on disk, file model\FlinkMOA_1519228325597_seed-3.model
3> Saving model on disk, file model\FlinkMOA_1519228325603_seed-2.model
2> HT with OzaBag seed 0 already processed 2000000 examples
1> HT with OzaBag seed 3 already processed 2500000 examples
4> HT with OzaBag seed 1 already processed 2000000 examples
3> HT with OzaBag seed 2 already processed 2500000 examples
1> HT with OzaBag seed 3 already processed 3000000 examples
2> HT with OzaBag seed 0 already processed 2500000 examples
4> HT with OzaBag seed 1 already processed 2500000 examples
3> HT with OzaBag seed 2 already processed 3000000 examples
1> HT with OzaBag seed 3 already processed 3500000 examples
2> HT with OzaBag seed 0 already processed 3000000 examples
3> HT with OzaBag seed 2 already processed 3500000 examples
4> HT with OzaBag seed 1 already processed 3000000 examples
1> HT with OzaBag seed 3 already processed 4000000 examples
2> HT with OzaBag seed 0 already processed 3500000 examples
3> HT with OzaBag seed 2 already processed 4000000 examples
2> Saving model on disk, file model\FlinkMOA_1519228385662_seed-0.model
4> Saving model on disk, file model\FlinkMOA_1519228385664_seed-1.model
1> Saving model on disk, file model\FlinkMOA_1519228385695_seed-3.model
3> Saving model on disk, file model\FlinkMOA_1519228385825_seed-2.model
4> HT with OzaBag seed 1 already processed 3500000 examples
1> HT with OzaBag seed 3 already processed 4500000 examples
...
```
