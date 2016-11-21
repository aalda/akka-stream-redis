lazy val `akka-stream-redis` =
  project
    .in(file("."))
    .settings(
      name := "akka-stream-redis"
    )
    .enablePlugins(AutomateHeaderPlugin)