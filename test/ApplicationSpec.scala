import models.Quote
import org.specs2.mutable._
import play.api.Logger

import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json.{JsString, JsObject, Json}

/**
 * Test repositioning operations.
 */
class ApplicationSpec extends Specification {

  "The model" should {
    "initially contain two quotes" in
      new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase("test"))) {

        Quote.list.take(2).map(_.english) must beEqualTo(List(
          "I will kill the men in iron suits",
          "She will speak before me; until then you will keep yourself quiet"
        ))
      }
  }

  "The router" should {
    "respond to GET translate action" in
      new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase("test"))) {
        val Some(result) = route(FakeRequest(GET, "/translate/test"))

        status(result) must equalTo(OK)
      }

    "respond to POST translate action" in
      new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase("test"))) {
        val json = JsObject(Seq("word" -> JsString("test")))
        val request = FakeRequest(POST, "/translate").withJsonBody(json).withHeaders((CONTENT_TYPE,"application/json"))
        val Some(result) = route(FakeRequest(POST, "/translate"))

        status(result) must equalTo(OK)
      }

    "respond to index action" in
      new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase("test"))) {
        val Some(result) = route(FakeRequest(GET, "/"))

        status(result) must equalTo(OK)
      }
  }

}