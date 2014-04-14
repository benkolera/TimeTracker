import scala.concurrent.duration._
import play.api.Application
import play.api.GlobalSettings

object Global extends GlobalSettings {

  val sched = new QuartzScheduler()

  override def onStart(app: Application) {
    sched.start()
    sched.schedule("Mailer", reminder(app)) at "0 0 17 ? * MON-FRI"
  }

  override def onStop(app: Application) {
    sched.stop()
  }

  def reminder( implicit application: Application ) = {
    play.api.db.slick.DB.withSession{ implicit s:scala.slick.session.Session =>
      models.DB.sendReminderNotifications
    }
  }

}
