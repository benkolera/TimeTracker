import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "TimeTracker"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.typesafe.slick" %% "slick" % "1.0.0-RC2"
    , "org.scalaj" % "scalaj-time_2.10.0-M7" % "0.6"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions ++= Seq("-Xfatal-warnings","-feature")
  ).dependsOn(RootProject( uri("git://github.com/benkolera/play-slick.git") ))

}
