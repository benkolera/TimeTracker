package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json


object Application extends Controller {

  import play.api.db.slick.DB
  import play.api.Play.current

  case class Log(
    category: String
    , hours: Int
    , minutes: Int
  )

  val logMapping = mapping(
    "category"  -> nonEmptyText( maxLength = 50 )
    , "hours"   -> number( 0, 24 )
    , "minutes" -> number( 0, 59 )
  )(Log.apply)(Log.unapply)

  case class DayLog( 
    username: String 
    , date: java.sql.Date
    , logs: List[Log]
  )

  val dayForm = Form( 
    mapping(
      "username" -> nonEmptyText( maxLength = 25 )
      , "date"   -> sqlDate("yyyy-MM-dd")
      , "logs"   -> list(logMapping)
    )(DayLog.apply)(DayLog.unapply)
  )

  def userOptions(implicit s: scala.slick.session.Session) = 
    models.DB.Users.all.map( 
      u => (u.username , s"${u.firstName} ${u.lastName}" ) 
    )

  def index( user: Option[String] ) = Action { implicit req =>
    DB.withSession{ implicit s =>
      Ok(views.html.index(dayForm.fill(
        DayLog(
          user.getOrElse("")
          , new java.sql.Date(new java.util.Date().getTime )
          , Nil
        )
      ),userOptions))
    }
  }

  def categoriesAcJs( term: String ) = Action {
    DB.withSession{ implicit s =>
      Ok( Json.toJson( models.DB.TimeLogs.searchCategories( term ) ) )
    }
  }

  def doLog = Action { implicit req => 
    DB.withSession{ implicit s =>
      dayForm.bindFromRequest.fold( 
        errForm  => BadRequest( 
          views.html.index( errForm , userOptions ) 
        ) 
        , dayLog => {
          models.DB.TimeLogs.insertMany(dayLog.logs.map { log =>
            models.DB.TimeLog( 
              date = dayLog.date
              , username = dayLog.username
              , category = log.category
              , hours = log.hours
              , minutes = log.minutes
            )
          } )
          Redirect( routes.Application.userHistory( 
            dayLog.username 
          ) ).flashing(
            "message" -> s"Your time for ${dayLog.date} has been recorded!"
          )
        }
      )
    }
  }

  def history = Action { implicit req =>
    DB.withSession{ implicit s =>
      Ok( views.html.history.index( 
        models.DB.Users.all.map( 
          u => (u.username , s"${u.firstName} ${u.lastName}")
        )
        , models.DB.TimeLogs.searchCategories( "" )
      ) )
    }
  }
  

  def userHistory(user: String) = Action { implicit req =>
    DB.withSession{ implicit s =>
      Ok( views.html.history.summary( 
        s"User:$user"
        , models.DB.TimeLogs.dateSummaryForUser( user )
        , flash.get("message")
      ) )
    }
  }

  def catHistory(cat: String) = Action { implicit req =>
    DB.withSession{ implicit s =>
      Ok( views.html.history.summary( 
        s"Category:$cat"
        , models.DB.TimeLogs.dateSummaryForCategory( cat )
        , None
      ) )
    }
  }

  def noLogsForToday = Action { implicit req =>
    DB.withSession{ implicit s =>
      models.DB.sendReminderNotifications
      Ok( "SENT" )
    }
  }
}
