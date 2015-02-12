package utils

import play.api.libs.json._

/**
 * Created by aleksandramalinowska on 2015-02-12.
 */
object JsonReader {

  def readDothrakiDictionary(filePath: String): Map[String, Snippet] = {
    buildDothrakiDictionary(convertToSnippets(readJson(filePath)))
  }

  def readEnglishDictionary(filePath: String): Map[String, Snippet] = {
    buildEnglishDictionary(convertToSnippets(readJson(filePath)))
  }

  def buildDothrakiDictionary(snippets: List[Snippet]): Map[String, Snippet] = snippets.map(snippet => (snippet.dothraki, snippet)).toMap

  def buildEnglishDictionary(snippets: List[Snippet]): Map[String, Snippet] = snippets.map(snippet => (snippet.english, snippet)).toMap

  def convertToSnippets(jsonArray: JsValue): scala.List[_root_.utils.Snippet] = {
    jsonArray.as[List[JsValue]].map(Snippet.reads)
  }

  def convertToSnippet(json: JsValue): Snippet = {
    // new Snippet("english", "dothraki", "speechPart", "pastSG", "quoteEnglish", "quoteDothraki")
    Snippet.reads(json)
  }

  def readJson(filePath: String): JsValue = {
    val source = scala.io.Source.fromFile(filePath)
    val lines = source.getLines mkString "\n"
    source.close()
    Json.parse(lines)
  }
}
