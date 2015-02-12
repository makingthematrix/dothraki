package utils

import play.api.libs.json._

/**
 * Created by aleksandramalinowska on 2015-02-12.
 */
object JsonReader {
  // new Snippet("english", "dothraki", "speechPart", "pastSG", "quoteEnglish", "quoteDothraki")

  def convertToSnippets(jsonArray: JsValue): scala.List[_root_.utils.Snippet] = {
    jsonArray.as[List[JsValue]].map(Snippet.reads)
  }

  def convertToSnippet(json: JsValue): Snippet = {
    Snippet.reads(json)
  }

  def readJson(filePath: String): JsValue = {
    val source = scala.io.Source.fromFile(filePath)
    val lines = source.getLines mkString "\n"
    source.close()
    Json.parse(lines)
  }
}
