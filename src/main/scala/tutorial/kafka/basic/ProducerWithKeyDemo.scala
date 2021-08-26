package tutorial.kafka.basic

import org.apache.kafka.clients.admin.{ Admin, NewTopic, TopicDescription }
import org.apache.kafka.clients.producer.ProducerConfig.{ BOOTSTRAP_SERVERS_CONFIG, KEY_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER_CLASS_CONFIG }
import org.apache.kafka.clients.producer.{ Callback, KafkaProducer, ProducerRecord, RecordMetadata }
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

import scala.jdk.FutureConverters._
import scala.jdk.CollectionConverters._
import java.util.concurrent.{ CompletableFuture, Future => JavaFuture }
import java.util
import java.util.{ Properties, UUID, concurrent }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.jdk.CollectionConverters.MapHasAsJava
import scala.util.{ Failure, Success }

object ProducerWithKeyDemo {
  private val logger = LoggerFactory.getLogger(ProducerHelloWorldDemo.getClass)
  private val config: Map[String, AnyRef] = Map(BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092")
  private val stringSerializer = classOf[StringSerializer].getName
  private val producerConfig = (config ++ Map(KEY_SERIALIZER_CLASS_CONFIG -> stringSerializer, VALUE_SERIALIZER_CLASS_CONFIG -> stringSerializer)).asJava

  // TODO: It is not working some reason needs to be investigated why it is not working
  def createTopic(name: String, partitions: Int = 1, replicationFactor: Short = 1)(implicit ec: ExecutionContext): Future[Unit] = {
    val admin = Admin.create(config.asJava)
    val newTopic = new NewTopic(name, partitions, replicationFactor)
    val result: JavaFuture[Void] = admin.createTopics(util.Arrays.asList(newTopic)).values().get(name)
    Future(result.get()) // TODO: is it a correct way to handle blocking call?
  }

  def deleteTopic(name: String)(implicit ec: ExecutionContext): Future[Unit] = {
    val promise = Promise[Unit]()
    val admin = Admin.create(config.asJava)
    val result = admin.deleteTopics(util.Arrays.asList(name)).values().get(name)
    result.get()
    Future(result.get()) // TODO: is it a correct way to handle blocking call?
  }

  def describeTopic(name: String)(implicit ec: ExecutionContext): Future[TopicDescription] = {
    val admin = Admin.create(config.asJava)
    val topicDescriptionJavaFuture = admin.describeTopics(util.Arrays.asList(name)).values().get(name)
    Future(topicDescriptionJavaFuture.get()) // TODO: is it a correct way to handle blocking call?
  }

  def sendMessage(key: String, message: String, topic: String)(implicit ec: ExecutionContext): Future[RecordMetadata] = {
    val producer = new KafkaProducer[String, String](producerConfig)
    val promise = Promise[RecordMetadata]()
    producer.send(
      new ProducerRecord[String, String](topic, key, message),
      new Callback {
        override def onCompletion(m: RecordMetadata, exception: Exception): Unit = {
          if (exception == null) {
            logger.info(s"${m.topic()}, partition: ${m.partition()}, offset: ${m.offset()}, timestamp: ${m.timestamp()}")
            promise.success(m)
          } else promise.failure(exception)
        }
      }
    )

    promise.future
  }

  def main(args: Array[String]): Unit = {
    val topic = "second-topic"
    import scala.concurrent.ExecutionContext.Implicits.global
    val randomTopic = UUID.randomUUID().toString
    val result: Future[RecordMetadata] = for {
      _ <- createTopic(randomTopic, 6, 2)
      topicDescription <- describeTopic(randomTopic)
      _ <- deleteTopic(randomTopic)
      metadata <- sendMessage("id-1", "second-hello-world", topic)
    } yield {
      logger.info("-" * 40)
      logger.info(s"Random topic ${topicDescription.name()}")
      logger.info(s"partitions: ${topicDescription.partitions().size()} ")
      logger.info("-" * 40)
      metadata
    }

    result.onComplete {
      case Success(m)         => logger.info(s"${m.topic()}, partition: ${m.partition()}, offset: ${m.offset()}, timestamp: ${m.timestamp()}")
      case Failure(exception) => logger.error("Error while sending message", exception)
    }

  }
}
