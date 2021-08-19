import sbt._

object Dependencies {
  private val AkkaVersion = "2.6.15"
  private val ScalaTestVersion = "3.2.9"

  // https://github.com/apache/kafka
  lazy val kafka = "org.apache.kafka" % "kafka-clients" % "2.8.0"

  // https://github.com/akka/akka
  lazy val akka = "com.typesafe.akka" %% "akka-actor" % AkkaVersion

  // https://github.com/akka/akka
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % AkkaVersion

  // https://github.com/scalatest/scalatest
  lazy val scalaTest = "org.scalatest" %% "scalatest" % ScalaTestVersion

  // https://github.com/qos-ch/logback
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.5"

}
