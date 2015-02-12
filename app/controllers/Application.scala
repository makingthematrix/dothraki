package controllers

import models.Quote
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json
import play.api.mvc.{Action, Controller}
import play.api.libs.json.{JsValue, JsString, JsError, Json}
import play.api.mvc.{BodyParsers, Action, Controller}
import utils.{Snippet, JsonReader}

import scala.collection.mutable.ArrayBuffer
import scala.util.{Success, Try, Failure}

object Application extends Controller {

  var dictionary = Map[String,Snippet] ()

  def index = Action {
    if(dictionary.isEmpty) dictionary = JsonReader.readEnglishDictionary("public/dictionary.json")
    Logger.debug("/ route");
    Ok(views.html.index(Quote.list))
  }

  def getTranslate(source: String) = Action {
    Ok(views.html.translate(source))
  }

  def postTranslate = Action {
    implicit request => {
      val mapOpt = request.body.asFormUrlEncoded
      val translation = mapOpt match {
        case Some(map) =>
          val word = map("word").mkString("")
          dictionary(word).dothraki
        case None => "unknown"
      }

      Logger.debug(request.body.asFormUrlEncoded.toString())
      //val jsValue:JsValue = request.body
      //println(jsValue.toString())
      //val tr = reads(jsValue)
      //val translation = tr.word
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