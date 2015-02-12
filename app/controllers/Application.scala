package controllers

import models.Quote
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

import scala.util.{Success, Try, Failure}

object Application extends Controller {

  def index = Action {
    Logger.debug("/ route");
    Ok(views.html.index(Quote.list))
  }

}