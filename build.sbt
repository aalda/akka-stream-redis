lazy val `akka-stream-redis` =
  project
    .in(file("."))
    .settings(
      name := "akka-stream-redis",
      libraryDependencies ++= List(
        Library.akkaSlf4j,
        Library.akkaStreams,
        Library.akkaStreamsTestkit,
        Library.logbackClassic,
        Library.redisScala,
        Library.scalatest
      )
    )
    .enablePlugins(AutomateHeaderPlugin)