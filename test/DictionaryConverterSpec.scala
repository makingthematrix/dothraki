import utils.DictionaryConverter

import org.specs2.mutable._

/**
 * Created by gorywoda on 06.02.15.
 */
class DictionaryConverterSpec extends Specification {
  "Dictionary Converter" should {
    "strip long quotes in one word" in {
      DictionaryConverter.stripLongQuotes("'''word'''") must beEqualTo("word")
    }

    "strip long quotes in a sentence" in {
      val sentence = "This is an example sentence"
      DictionaryConverter.stripLongQuotes(s"'''$sentence'''") must beEqualTo(sentence)
    }

    "read dictionary file" in {
      val dictionary = DictionaryConverter.readText("public/dictionary.txt")
      println("Dictionary length: " + dictionary.length + "\n")
      dictionary.length must beGreaterThan(0)
    }

    "get translation from snippet" in {
      val snippet = DictionaryConverter.readText("public/snippet1.txt")
      snippet.length must beGreaterThan(0)
      val (dothraki, english) = DictionaryConverter.getTranslation(snippet)
      dothraki must beEqualTo("acchakat")
      english must beEqualTo("to silence")
    }

    "find pastSG in snippet" in {
      val snippet = DictionaryConverter.readText("public/snippet1.txt")
      snippet.length must beGreaterThan(0)
      val pastSGOpt = DictionaryConverter.findPastSG(snippet)
      DictionaryConverter.assert(pastSGOpt != None, "pastSGOpt should not be None")
      pastSGOpt.get must beEqualTo("acchak")
    }

    "find quote in snippet" in {
      val snippet = DictionaryConverter.readText("public/snippet1.txt")
      snippet.length must beGreaterThan(0)
      val quoteOpt = DictionaryConverter.findQuote(snippet)
      DictionaryConverter.assert(quoteOpt != None, "quoteOpt should not be None")
      val (dothraki, english) = quoteOpt.get
      dothraki must beEqualTo("Me vastoe hatif anni; ahhazaan yer nemo vacchaki.")
      english must beEqualTo("She will speak before me; until then you will keep yourself quiet.")
    }

    "get speech part" in {
      val snippet = DictionaryConverter.readText("public/snippet1.txt")
      snippet.length must beGreaterThan(0)
      val speechPart = DictionaryConverter.getSpeechPart(snippet)
      speechPart must beEqualTo("vtr")
    }

    "split to snippets" in {
      val dictionary = DictionaryConverter.readText("public/dictionary.txt")
      dictionary.length must beGreaterThan(0)
      val snippets = DictionaryConverter.parseSnippets(dictionary)
      println("Snippets: " + snippets.size + "\n")
      snippets.size must beGreaterThan(0)
    }

    "get snippets as snippets" in {
      val dictionary = DictionaryConverter.readText("public/dictionary.txt")
      dictionary.length must beGreaterThan(0)
      val strSnippets = DictionaryConverter.parseSnippets(dictionary)
      strSnippets.size must beGreaterThan(0)
      val snippets = DictionaryConverter.snippets(dictionary)
      snippets.size must beEqualTo(strSnippets.size)
    }

    "write something to file" in {
      val text =
        """
          |*'''khogar''' [xogar] <span id="khogar"></span>
          |:''ni.'' word for one's apparel, clothes
          |:{{Quote|Anha vaddrivak mahrazhis fini ondee khogar shiqethi.|I will kill the men in iron suits}}
        """.stripMargin
      DictionaryConverter.writeToFile("public/snippet_x.txt", text)
      val str = DictionaryConverter.readText("public/snippet_x.txt")
      val snippet = DictionaryConverter.convertToSnippet(str)
      snippet.dothraki must beEqualTo("khogar")
      snippet.english must beEqualTo("word for one's apparel, clothes")
      snippet.speechPart must beEqualTo("ni")
      snippet.pastSG must beEqualTo("")
      snippet.quoteDothraki must beEqualTo("Anha vaddrivak mahrazhis fini ondee khogar shiqethi.")
      snippet.quoteEnglish must beEqualTo("I will kill the men in iron suits")
    }

    "convert snippet to json" in {
      val str = DictionaryConverter.readText("public/snippet1.txt")
      val snippet = DictionaryConverter.convertToSnippet(str)
      val json = DictionaryConverter.convertToJson(snippet)
      println("JSON: " + json + "\n")
      json.size must beGreaterThan(0)
    }

    "convert dictionary to json" in {
      val dictionary = DictionaryConverter.readText("public/dictionary.txt")
      val snippets = DictionaryConverter.snippets(dictionary)
      val json = DictionaryConverter.convertToJson(snippets)
      println(s"dictionary size: ${dictionary.size}, json size: ${json.size}")
      json.size must beGreaterThan(0)
    }

    "convert dictionary to json format" in {
      val result = DictionaryConverter.writeAsJson("public/dictionary.txt","public/dictionary.json")
      result must beEqualTo(true)
    }
  }
}
