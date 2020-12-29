package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   入口Action，主要操作都放在ApplicationController里边了
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())//这个index.html继承自main.html
    //Ok("Welcome to Play")
  }
}
