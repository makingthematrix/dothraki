package utils

import scala.collection.mutable
import java.io.{BufferedWriter, File, FileWriter, IOException}
import play.api.libs.json._

/**
  * Created by gorywoda on 06.02.15.
 */
object DictionaryConverter {

  def writeAsJson(from: String, to: String) = {
    val dictionary = readText(from)
    val json = convertToJson(snippets(dictionary))
    writeToFile(to, json)
    true
  }

  def convertToJson(snippet: Snippet) = Json.toJson(snippet).toString()

  def convertToJson(snippets: List[Snippet]) = Json.toJson(snippets).toString()

  def writeToFile(filePath: String, text: String) = {
    var writer:BufferedWriter = null
    try {
      val file = new File(filePath)
      if (!file.exists()) {
        file.createNewFile()
        file.setReadable(true)
        file.setWritable(true)
      }
      writer = new BufferedWriter(new FileWriter(file, true))
      writer.write(text)
      writer.newLine()
      writer.flush()
    } catch {
      case ex: IOException => ex.printStackTrace()
    } finally {
      if(writer != null) writer.close()
    }
  }

  def convertToSnippet(snippet: String) = {
    val (dothraki, english) = getTranslation(snippet)
    val speechPart = getSpeechPart(snippet)
    val pastSG = findPastSG(snippet) match {
      case Some(str) => str
      case None => ""
    }
    val (quoteDothraki, quoteEnglish) = findQuote(snippet) match {
      case Some(tuple) => tuple
      case None => ("","")
    }
    Snippet(english, dothraki, speechPart, pastSG, quoteEnglish, quoteDothraki)
  }

  def snippets(dictionary: String):List[Snippet] = parseSnippets(dictionary).map(convertToSnippet(_))

  def parseSnippets(dictionary: String):List[String] = {
    val buffer = mutable.ListBuffer[String]()
    var start = dictionary.indexOf('*')
    while(start != -1){
      val end = dictionary.indexOf('*', start + 1)
      buffer += (if(end == -1) dictionary.substring(start) else dictionary.substring(start, end))
      start = end
    }
    buffer.toList
  }

  def getSpeechPart(snippet: String) = {
    val (_, dothrakiEnd, _, _) = findSnippetIndices(snippet)
    val speechPartStart = snippet.indexOf(":''", dothrakiEnd) + 3
    val speechPartEnd = snippet.indexOf("''", speechPartStart)
    val t = snippet.substring(speechPartStart, speechPartEnd)
    if(t.endsWith(".")) t.substring(0, t.length - 1) else t
  }

  def findQuote(snippet: String):Option[(String,String)] = {
    val (_, _, _, englishEnd) = findSnippetIndices(snippet)
    val quoteStart = snippet.indexOf(":{{Quote|",englishEnd)
    if(quoteStart != -1) {
      val quoteEnd = snippet.indexOf("}}", quoteStart + 9)
      val quote = snippet.substring(quoteStart + 9, quoteEnd)
      val marker = quote.indexOf('|')
      Some((quote.substring(0,marker), quote.substring(marker+1,quote.length)))
    } else None
  }

  private def findSnippetIndices(snippet: String):(Int,Int,Int,Int) = {
    val dothrakiStart = snippet.indexOf("'''")
    val dothrakiEnd = snippet.indexOf("'''", dothrakiStart + 3)
    val t1 = snippet.indexOf(":''", dothrakiEnd)
    val englishStart = snippet.indexOf("'' ", t1) + 3
    val englishEnd = snippet.indexOf('\n', englishStart)
    (dothrakiStart, dothrakiEnd, englishStart, englishEnd)
  }

  def findPastSG(snippet: String):Option[String] = {
    val (_, _, _, englishEnd) = findSnippetIndices(snippet)
    val pastSGStart = snippet.indexOf(":{{Vsup|",englishEnd)
    if(pastSGStart != -1) {
      val pastSGEnd = snippet.indexOf("}}", pastSGStart + 8)
      Some(snippet.substring(pastSGStart + 8, pastSGEnd))
    } else None
  }

  def getTranslation(snippet: String):(String, String) = {
    val (dothrakiStart, dothrakiEnd, englishStart, englishEnd) = findSnippetIndices(snippet)
    val dothraki = stripLongQuotes(snippet.substring(dothrakiStart,dothrakiEnd))
    val english = snippet.substring(englishStart,englishEnd)
    (dothraki, english)
  }

  def stripQuotes(str: String, quotes: String) = {
    val t1 = if(str.startsWith(quotes)) str.substring(quotes.length) else str
    val t2 = if(t1.endsWith(quotes)) t1.substring(0, t1.length - quotes.length) else t1
    t2.trim
  }

  def stripLongQuotes(str: String) = stripQuotes(str, "'''")
  def stripShortQuotes(str: String) = stripQuotes(str, "''")

  def fail(str: String):IllegalArgumentException = throw new IllegalArgumentException(str)
  def assert(condition: => Boolean, str: String) = if(!condition) fail(str)

  def readText(filePath: String) = {
    val source = scala.io.Source.fromFile(filePath)
    val lines = source.getLines mkString "\n"
    source.close()
    lines
  }
}
