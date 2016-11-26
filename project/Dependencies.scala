import sbt._

object Version {
  final val Akka = "2.4.12"
  final val Logback = "1.1.7"
  final val RedisScala = "1.7.0"
  final val Scala = Seq("2.11.8", "2.12.0")
  final val Scalatest = "3.0.0"
}

object Library {
  val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % Version.Akka % Test
  val akkaStreams = "com.typesafe.akka" %% "akka-stream" % Version.Akka
  val akkaStreamsTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % Version.Akka % Test
  val logbackClassic = "ch.qos.logback" % "logback-classic" % Version.Logback % Test
  val redisScala = "com.github.etaty" %% "rediscala" % Version.RedisScala
  val scalatest = "org.scalatest" %% "scalatest" % Version.Scalatest % Test
}
