package tutorial.kafka.stream

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ Serdes, StringSerializer }
import org.apache.kafka.streams.kstream.{ KStream, KTable, KeyValueMapper, Produced, ValueMapper }
import org.apache.kafka.streams.{ KafkaStreams, StreamsBuilder, StreamsConfig }

import java.{ lang, util }
import java.util.Properties
import scala.jdk.CollectionConverters.IterableHasAsJava

class WordCount(appId: String, bootStrapServer: String) {
  // TODO: Very annoying due to types miss match, I wish kafka streams exposes scala API as well.
  // After learning kafka, I might use alpakka than java api
  // future experiment
  private val splitWords: ValueMapper[String, lang.Iterable[String]] = _.split("""\W+""").toList.asJava

  def streamer(inputTopic: String, outputTopic: String): KafkaStreams = {
    val streamBuilder: StreamsBuilder = new StreamsBuilder()
    val wordCounts: KTable[String, lang.Long] = streamBuilder
      .stream[String, String](inputTopic)
      .mapValues(line => line.toLowerCase)
      .flatMapValues(splitWords)
      .groupBy((_, word) => word)
      .count()
    wordCounts.toStream().to(outputTopic, Produced.`with`(Serdes.String(), Serdes.Long()))
    new KafkaStreams(streamBuilder.build(), config())
  }

  private def config(): Properties = {
    val config = new Properties()
    // Very important this will be used for group-id and internal topic prefixes
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, appId)
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer)
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    config
  }
}

object WordCountApp {

  def main(args: Array[String]): Unit = {
    val wordCount = new WordCount("wordcount-app", "localhost:9092")
    val streams = wordCount.streamer("word-count-in", "word-count-out")
    streams.start()
    sys.addShutdownHook(streams.close())
    println("=" * 50)
    println(streams.toString)
    println("=" * 50)
  }
}
