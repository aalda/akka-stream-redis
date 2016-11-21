import sbt._
import Keys._
import de.heikoseeberger.sbtheader.HeaderPlugin
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
import de.heikoseeberger.sbtheader.license.Apache2_0
import org.scalafmt.sbt.ScalaFmtPlugin
import org.scalafmt.sbt.ScalaFmtPlugin.autoImport._
import sbt.AutoPlugin
import sbt.plugins.JvmPlugin

object Build extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = JvmPlugin && HeaderPlugin && ScalaFmtPlugin

  override lazy val projectSettings = reformatOnCompileSettings ++
    Seq(
      // Core settings
      organization := "io.github.aalda",
      licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
      mappings.in(Compile, packageBin) += baseDirectory.in(ThisBuild).value / "LICENSE" -> "LICENSE",
      scalaVersion := crossScalaVersions.value.head,
      crossScalaVersions := Version.Scala,
      crossVersion := CrossVersion.binary,
      scalacOptions ++= Vector(
        "-unchecked",
        "-deprecation",
        "-language:_",
        "-target:jvm-1.8",
        "-encoding", "UTF-8",
        "-Xlint",
        "-Yno-adapted-args",
        "-Ywarn-dead-code",
        "-Xfuture"
      ),

      unmanagedSourceDirectories.in(Compile) := Vector(scalaSource.in(Compile).value),
      unmanagedSourceDirectories.in(Test) := Vector(scalaSource.in(Test).value),

      // POM settings for Sonatype
      homepage := Some(url("https://github.com/aalda/akka-stream-redis")),
      scmInfo := Some(ScmInfo(url("https://github.com/aalda/akka-stream-redis"),
        "git@github.com:aalda/akka-stream-redis.git")),
      developers += Developer("aalda",
        "Alvaro Alda",
        "",
        url("https://github.com/aalda")),
      pomIncludeRepository := (_ => false),

      // scalafmt settings
      formatSbtFiles := false,
      scalafmtConfig := Some(baseDirectory.in(ThisBuild).value / ".scalafmt.conf"),
      ivyScala := ivyScala.value.map(_.copy(overrideScalaVersion = sbtPlugin.value)), // TODO Remove once this workaround no longer needed (https://github.com/sbt/sbt/issues/2786)!

      // Header settings
      headers := Map("scala" -> Apache2_0("2016", "Alvaro Alda"))

    )
}