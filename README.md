# Meces
Meces is a rescaling mechanism for stateful distributed stream processing systems.
This is the repository for Meces's implementation based on Apache Flink 1.12.0.
## Meces on Flink
Meces code is in directory ```Meces-on-Flink```. Meces is compiled and deployed in the same way as Flink.


To compile Meces:
```
mvn clean install -DskipTests -Dfast
```

To start a cluster:
```
cd Meces-on-Flink/build-target
./bin/start-cluster.sh
```

To stop the cluster:
```
cd Meces-on-Flink/build-target
./bin/stop-cluster.sh
```

## Experiments
### benchmark jobs
### Megaphone on Flink
### Rhino on Flink
