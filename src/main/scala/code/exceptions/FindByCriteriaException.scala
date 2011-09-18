package code.exceptions

import java.lang.String
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import xml.Node
import net.liftweb.json.Xml

/**
 * This class is used when no entities was found in the database
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class FindByCriteriaException(msg: String = "Can not find item by given criteria!") extends Exception with FormatedException{

  val code = 400
  def getErrorName: String = this.getClass.getName

  override def getMessage: String = msg

  def getErrorAsJson: JValue = {
    ("error" -> (
      ("name" -> this.getErrorName) ~
        ("message" -> this.getMessage)
      ))
  }

  def getErrorAsXml: Node = {
    Xml.toXml(getErrorAsJson).head
  }

  def getHttpStatusCode = code
}
