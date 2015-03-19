package models

import models.db.Quotes
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.db.slick.Session

case class Quote(id: Option[Int], english: String, dothraki: String)

/**
 * Data access functions.
 */
object Quote {

  // Base Slick database query to use for data access.
  val quotes = TableQuery[Quotes]

  /**
   * Returns a list of animals, sorted by position.
   */
  def list = DB.withSession { implicit s: Session =>
    quotes.sortBy( q => q.english ).list
  }

}