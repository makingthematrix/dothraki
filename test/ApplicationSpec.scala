import models.Quote
import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

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

}