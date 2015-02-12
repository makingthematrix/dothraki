package controllers

import models.Quote
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json
import play.api.mvc.{Action, Controller}
import play.api.libs.json.{JsString, JsError, Json}
import play.api.mvc.{BodyParsers, Action, Controller}

import scala.util.{Success, Try, Failure}

object Application extends Controller {

  def index = Action {
    Logger.debug("/ route");
    Ok(views.html.index(Quote.list))
  }

  def getTranslate(source: String) = Action {
    Ok(views.html.translate(source))
  }

  def postTranslate = Action(BodyParsers.parse.json) { implicit request =>
    val wordResult = request.body.validate[String]

    Logger.debug(wordResult.toString)

    wordResult.map {
      case word: String => Ok(Json.obj("status" -> "OK", "translation" -> JsString(word)))
    }.recoverTotal {e => BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(e)))}
  }
}