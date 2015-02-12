import org.specs2.mutable._
import play.libs.Json
import utils.{Snippet, JsonReader}

/**
 * Created by aleksandramalinowska on 2015-02-12.
 */
class JsonReaderSpec extends Specification {
  "JSON Reader" should {
    "read JSON from file" in {
      val json = JsonReader.readJson("public/json1.json")

      val expectedJson = Json.parse("{\"english\":\"to silence\",\"dothraki\":\"acchakat\",\"speechPart\":\"vtr\",\"pastSG\":\"acchak\",\"quoteEnglish\":\"She will speak before me; until then you will keep yourself quiet.\",\"quoteDothraki\":\"Me vastoe hatif anni; ahhazaan yer nemo vacchaki.\"}")

      json.toString must beEqualTo(expectedJson.toString)
    }

    "convert single JSON node to Snippet" in {
      val json = JsonReader.readJson("public/json1.json")
      val snippet = JsonReader.convertToSnippet(json)
      snippet.english must beEqualTo("to silence")
    }

    "convert JSON array to Snippets" in {
      val jsonArray = JsonReader.readJson("public/json2.json")
      val snippets: List[Snippet] = JsonReader.convertToSnippets(jsonArray)
      snippets.size must beGreaterThan(0)
      snippets.head.english must beEqualTo("to silence")
      snippets.tail.head.english must beEqualTo("smelly")
    }
  }
}
