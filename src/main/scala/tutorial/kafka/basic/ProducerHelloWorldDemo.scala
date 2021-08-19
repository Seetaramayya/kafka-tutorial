package tutorial.kafka.basic

import org.apache.kafka.clients.producer.{ Callback, KafkaProducer, Producer, ProducerRecord, RecordMetadata }
import org.apache.kafka.clients.producer.ProducerConfig._
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

import java.util.Properties

/**
 * https://docs.confluent.io/clients-kafka-java/current/overview.html#java-example-code
 */
object ProducerHelloWorldDemo {
  private val logger = LoggerFactory.getLogger(ProducerHelloWorldDemo.getClass)
  def main(args: Array[String]): Unit = {
    val topic = "first-topic"
    val config = new Properties()
    config.setProperty(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    config.setProperty(KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    config.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    val producer = new KafkaProducer[String, String](config)
    logger.info("Starting...")

    val messagesToSend = (1 until 300).map(i => s"message $i").map(new ProducerRecord[String, String](topic, _))

    messagesToSend.foreach { messageToSend =>
      producer.send(
        messageToSend,
        (metadata: RecordMetadata, exception: Exception) => {
          if (exception == null) {
            logger.info(s"topic : ${metadata.topic()}, partition: ${metadata.partition()}, offset: ${metadata.offset()}, timestamp: ${metadata.timestamp()}")
          } else {
            exception.printStackTrace()
          }
        }
      )

    }

    producer.close()
  }

}
