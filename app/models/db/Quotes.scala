package models.db

import models.Quote
import play.api.db.slick.Config.driver.simple._

/**
 * Database table mapping, using standard Slick syntax.
 */
class Quotes(tag: Tag) extends Table[Quote](tag, "QUOTES") {

  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def english = column[String]("ENGLISH")
  def dothraki = column[String]("DOTHRAKI")

  // Slick requires a `*` ‘projection’, to define a row (tuple of columns).
  // `id` is mapped to an `Option[Int]`, hence the use of Slick’s ? method.
  def * = (id.?, english, dothraki) <> ((Quote.apply _).tupled, Quote.unapply)
}
