package controllers

import models.Quote
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json
import play.api.mvc.{Action, Controller}
import play.api.libs.json.{JsValue, JsString, JsError, Json}
import play.api.mvc.{BodyParsers, Action, Controller}

import scala.util.{Success, Try, Failure}

object Application extends Controller {

  case class TranslationRequest(word: String)

  implicit def reads(jsValue: JsValue) = TranslationRequest(
    (jsValue \ "word").as[String]
  )

  def index = Action {
    Logger.debug("/ route");
    Ok(views.html.index(Quote.list))
  }

  def getTranslate(source: String) = Action {
    Ok(views.html.translate(source))
  }

  def postTranslate = Action {
    implicit request => {
      val translation = request.body.asJson match {
        case Some(jsValue) => reads(jsValue).word
        case None => "Unknown value"
      }
      Ok(Json.obj("status" -> "OK", "translation" -> JsString(translation))).as("application/json")
    }
  }


  /* Action(BodyParsers.parse.json) { implicit request =>
    println(s"postTranslate")
    val wordResult = request.body.validate[String]

    println(wordResult.toString)

    wordResult.map {
      case word: String => Ok(Json.obj("status" -> "OK", "translation" -> JsString(word))).as("application/json")
    }.recoverTotal {e => BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(e)))}
  }*/
}