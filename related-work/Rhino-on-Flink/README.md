### Rhino on Flink

Rhino is published in [Rhino: Efficient Management of Very Large Distributed State for Stream Processing Engines](https://dl.acm.org/doi/10.1145/3318464.3389723) and proposes an  incremental state transfer mechanism for large state migration.

As Rhino's authors have not open-sourced it by the time we submit, we choose to implement a version based on designs described in its paper by ourselves on Flink. And we choose to redistribute state evenly in each instance during state migration.

#### Installation

Rhino on Flink is implemented on FLink so the compilation and deployment is the same as original Flink.

Compilation:

```
cd {{Rhino on Flink directory}}
mvn clean install -DskipTests -Dfast
```

Deployment:

```
cd {{Rhino on Flink directory}}/build-target
./bin/start-cluster.sh
```

Trigger state migration:

```
cd {{Rhino on Flink directory}}/build-target
./bin flink rescale {{job-id}}
```

#### Code organization

This implementation consists of two parts, namely incremental replication of state and state migration.  And the experiment job can be found in `RescaleWordCount.java`

#### Requirements

Our version of implementation relies on `Kafka` to synchronize replication/rescaling procedure and you should deploy Kafka on your cluster before running it.

We put the configuration file in `{{project_path}}/build-target/conf/rhino-conf` and you should configure it according to your own environment.

