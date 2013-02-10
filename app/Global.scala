import scala.concurrent.duration._
import play.api.Application
import play.api.GlobalSettings

import utils.QuartzScheduler

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    QuartzScheduler.start()
    QuartzScheduler.schedule("Mailer", reminder(app)) at "0 0 17 ? * MON-FRI"
  }

  override def onStop(app: Application) {
    QuartzScheduler.stop()  
  }

  def reminder( implicit application: Application ) = {
    import com.typesafe.plugin.{use,MailerPlugin}
    import play.api.db.slick.DB

    DB.withSession{ implicit s =>
      val mail = use[MailerPlugin].email

      for ( u <- models.DB.Users.withoutLogForToday ) {
        mail.setSubject("TimeTracker - Reminder to track time for today");
        mail.addRecipient(u.email);
        mail.addFrom("bkolera@iseek.com.au");

        val url = Seq(
          "http://"
          , java.net.InetAddress.getLocalHost().getHostName() 
          , ":9000"
          , controllers.routes.Application.index( 
            Some( u.username )
          ).toString
        ).mkString( "" ) 

        mail.send( 
          s"You haven't recorded time for today. Please record it here: $url"
          , s"You haven't recorded time for today. Please record it <a href='$url\'>here</a>."
        );
      }
    }
  }

}
