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

    "build English to Dothraki dictionary" in {
      val jsonArray = JsonReader.readJson("public/json2.json")
      val snippets: List[Snippet] = JsonReader.convertToSnippets(jsonArray)
      val dictionary: Map[String, Snippet] = JsonReader.buildEnglishDictionary(snippets)
      var result = true
      dictionary.foreach(tuple => result = (tuple._2.english must beEqualTo(tuple._1)) && result)
      result must beEqualTo(true)
    }

    "build Dothraki to English dictionary" in {
      val jsonArray = JsonReader.readJson("public/json2.json")
      val snippets: List[Snippet] = JsonReader.convertToSnippets(jsonArray)
      val dictionary: Map[String, Snippet] = JsonReader.buildDothrakiDictionary(snippets)
      var result = true
      dictionary.foreach(tuple => result = (tuple._2.dothraki must beEqualTo(tuple._1)) && result)
      result must beEqualTo(true)
    }

    "read English dictionary from file" in {
      val dictionary: Map[String, Snippet] = JsonReader.readEnglishDictionary("public/json2.json")
      var result = true
      dictionary.foreach(tuple => result = (tuple._2.english must beEqualTo(tuple._1)) && result)
      result must beEqualTo(true)
    }

    "read Dothraki dictionary from file" in {
      val dictionary: Map[String, Snippet] = JsonReader.readDothrakiDictionary("public/json2.json")
      var result = true
      dictionary.foreach(tuple => result = (tuple._2.dothraki must beEqualTo(tuple._1)) && result)
      result must beEqualTo(true)
    }
  }
}
