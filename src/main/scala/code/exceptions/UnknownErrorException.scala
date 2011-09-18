package code.exceptions

import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import xml.Node
import net.liftweb.json.Xml

/**
 * This class is used when an unknown error occurs
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class UnknownErrorException(msg: String = "An unknown error has occured!") extends Exception with FormatedException{
  def getErrorName: String = this.getClass.getName

  val code = 400
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