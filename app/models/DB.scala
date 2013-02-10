package models

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.{StaticQuery => Q}

object DB {

  import org.joda.time.format.PeriodFormatterBuilder
  import org.scala_tools.time.Imports._


  case class User ( username: String, firstName: String, lastName: String )

  object Users extends Table[User]("USERS") {
    def username  = column[String]("USERNAME", O.PrimaryKey)
    def firstName = column[String]("FIRST_NAME")
    def lastName  = column[String]("LAST_NAME")
    def * = username ~ firstName ~ lastName <> (User,User.unapply _)

    def all(implicit s: Session) = Query(DB.Users).list

  }

  case class TimeLog ( 
    id: Option[Int] = None
    , username: String
    , date: java.sql.Date
    , category: String
    , hours: Int
    , minutes: Int 
  )

  val formatter = new PeriodFormatterBuilder()
    .appendHours()
    .appendSuffix("h ")
    .appendMinutes()
    .appendSuffix("m")
    .toFormatter();

  case class TimeLogSummaryGroup ( 
    title: String
    , sumTime: String
    , lines: Seq[TimeLogSummaryLine]
  )

  case class TimeLogSummaryLine (
    name: String
    , time: String
  )

  object TimeLogs extends Table[TimeLog]("TIME_LOG") {
    def id       = column[Int]("ID", O.PrimaryKey, O.AutoInc )
    def username = column[String]("USERNAME")
    def date     = column[java.sql.Date]("DATE")
    def category = column[String]("CATEGORY")
    def hours    = column[Int]("HOURS")
    def minutes  = column[Int]("MINUTES")
  
    def * = id.? ~ username ~ date ~ category ~ hours ~ minutes <> (
      TimeLog,TimeLog.unapply _
    )

    def insertMany( tls: Seq[TimeLog] )(implicit s: Session) = 
      TimeLogs.insertAll( tls :_* )
  
    def forUser( user: String )(implicit s: Session) = 
      Query(TimeLogs).filter( 
        _.username === user 
      ).sortBy(
        _.date.asc
      ).list

    def forCategory( category: String )(implicit s: Session) = 
      Query(TimeLogs).filter( 
        _.category === category
      ).sortBy(
        _.date.asc
      ).list

    def logDuration( l: TimeLog ) = l.hours.hours + l.minutes.minutes

    def dateSummaryForUser( user: String )(implicit s:Session) =
      summarise( _.category , forUser( user ).groupBy( _.date.toString ) )

    def dateSummaryForCategory( category: String )(implicit s:Session) = 
      summarise( _.username, forCategory( category ).groupBy( _.date.toString ) )

    def summarise( f: (TimeLog=>String), map: Map[String,Seq[TimeLog]] ) = {
      (for ( (date, logs) <- map.toSeq.sortBy( _._1 ).reverse ) yield {
        val duration = logs.foldLeft( 0.hours )( 
          (acc,x) => acc + logDuration(x)
        )
        TimeLogSummaryGroup(
          date.toString
          , formatter.print( duration.toPeriod )
          , logs.map( l => TimeLogSummaryLine( 
            f(l) , formatter.print( logDuration( l ) )
          ) )
        )
      }).toSeq
    }



    def searchCategories(partial: String)(implicit s: Session):List[String] = 
      Q.query[String,String](
        "SELECT DISTINCT CATEGORY FROM TIME_LOG WHERE CATEGORY LIKE ? ORDER BY 1"
      ).list( s"%$partial%" )
  }

}
