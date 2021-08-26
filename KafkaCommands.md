# Install and start

  Downloaded kafka and put it in `apps` folder. Added path to `.bashrc` also exported `$KAFKA_HOME`

- Start Zookeeper (alias created in `~/bin/zookeeper-start` )

  ```
  zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties
  ```

- Start Kafka Server (alias created in `~/bin/kafka-start`, this alias creates 3 brokers)

  ```
  kafka-server-start.sh $KAFKA_HOME/config/server.properties
  ```

- Scripts assumes that there are `server0.properties`, `server1.properties` and `server2.properties` exists in `$KAFKA_HOME/config/`

- Reset offset to starting for given consumer group
  ```shell
     kafka-consumer-groups.sh --bootstrap-server localhost:9092 --reset-offsets --to-earliest --topic second-topic --group my-second-console-consumer
  ```

# Clean up

- Clean up kafka with `~/bin/kafka-cleanup` ( which deletes all folders in `/tmp/kafka-logs-*` and `/tmp/zookeeper`)


# Commands
- Useful command line tools

  - Creates topic with 3 partitions and single replica

    ```shell
     kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic test
     kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic test # zookeeper port is 2181
    ```
    
    __Note: `--zookeeper` flag is replaced with `--bootstrap-server`__

  - List topics

    ```
    kafka-topics.sh --list --bootstrap-server localhost:9092
    ```
    
  - Describe topic
  
  ```shell
  seeta@nl1mcl-524932  master  kafka-topics.sh --zookeeper localhost:2181 --topic fist-topic --describe                                                                      ∞
  Topic: fist-topic       PartitionCount: 3       ReplicationFactor: 3    Configs:
  Topic: fist-topic       Partition: 0    Leader: 0       Replicas: 0,1,2 Isr: 2,1,0
  Topic: fist-topic       Partition: 1    Leader: 1       Replicas: 1,2,0 Isr: 2,1,0
  Topic: fist-topic       Partition: 2    Leader: 2       Replicas: 2,0,1 Isr: 2,0,1
  ```

  - Send message to kafka cluster

    ```
    kafka-console-producer.sh --broker-list localhost:9092 --topic test
    ```

  - Consume message from the kafka cluster using console

    ```
    kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
    ```

  - Consume message from the kafka cluster in single consumer group using console which shares the work load,
    in other words only one of the node in the consumer group gets the message instead of all the nodes

    ```
    kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning --group consumer-group1
    ```

  - Dumps broker metrics using `kafka-dump-log.sh` file
    ```
    kafka-dump-log.sh --files /tmp/kafka-logs-0/test-1/00000000000000000000.log
    ```
