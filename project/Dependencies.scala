import sbt._

object Version {
  final val Akka = "2.4.12"
  final val Scala = Seq("2.11.8", "2.12.0")
  final val Scalatest = "3.0.0"
}

object Library {
  val akkaStreams = "com.typesafe.akka" %% "akka-stream" % Version.Akka
  val akkaStreamsTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % Version.Akka % Test
  val scalatest = "org.scalatest" %% "scalatest" % Version.Scalatest % Test
}