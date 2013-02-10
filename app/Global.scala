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
    play.api.db.slick.DB.withSession{ implicit s =>
      models.DB.sendReminderNotifications
    }
  }

}
