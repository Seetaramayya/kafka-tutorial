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
# Clean up

- Clean up kafka with `~/bin/kafka-cleanup` ( which deletes all folders in `/tmp/kafka-logs-*` and `/tmp/zookeeper`)


# Commands
- Useful command line tools

  - Creates topic with 3 partitions and single replica

    ```
    kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic test
    ```
  - List topics

    ```
    kafka-topics.sh --list --bootstrap-server localhost:9092
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
