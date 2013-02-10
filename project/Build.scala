import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "TimeTracker"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.typesafe.slick" %% "slick" % "1.0.0-RC2"
    , "org.scalaj" % "scalaj-time_2.10.0-M7" % "0.6"
    , "com.typesafe" %% "play-plugins-mailer" % "2.1.0"
    , "mchv" %% "play2-quartz" % "1.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions ++= Seq("-Xfatal-warnings","-feature")
    , resolvers += "Mariot Chauvin" at "http://mchv.me/repository"
  ).dependsOn(RootProject( uri("git://github.com/benkolera/play-slick.git") ))

}
