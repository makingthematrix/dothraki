package dothraki

import scala.scalajs.js
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.document


object Example extends js.JSApp {
  def main(): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = "sajkljsl"
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  @JSExport
  def addClickedMessage(): Unit = {
    appendPar(document.body, "You clicked the button!")
  }
}
