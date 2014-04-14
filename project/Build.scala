import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "TimeTracker"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.typesafe.slick" %% "slick" % "1.0.1"
    , "com.github.nscala-time" %% "nscala-time" % "0.8.0"
    , "com.typesafe" %% "play-plugins-mailer" % "2.1.0"
    , "com.typesafe.play" %% "play-slick" % "0.5.0.8"
    , "org.quartz-scheduler" % "quartz" % "2.1.6"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.4"
    , scalacOptions ++= Seq("-Xfatal-warnings","-feature")
    , resolvers += "Mariot Chauvin" at "http://mchv.me/repository"
  )
}
