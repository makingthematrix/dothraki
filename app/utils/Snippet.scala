package utils

import play.api.libs.json._

/**
 * Created by gorywoda on 06.02.15.
 */
case class Snippet(english: String, dothraki: String, speechPart: String, pastSG: String, quoteEnglish: String, quoteDothraki: String)

object Snippet {
  implicit val writes = new Writes[Snippet] {
    def writes(snippet: Snippet) = Json.obj(
      "english" -> snippet.english,
      "dothraki" -> snippet.dothraki,
      "speechPart" -> snippet.speechPart,
      "pastSG" -> snippet.pastSG,
      "quoteEnglish" -> snippet.quoteEnglish,
      "quoteDothraki" -> snippet.quoteDothraki
    )
  }
}
