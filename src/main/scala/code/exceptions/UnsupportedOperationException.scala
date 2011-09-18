package code.exceptions

import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import xml.Node
import net.liftweb.json.Xml

/**
 * This class is used when a function is not supported at this moment
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class UnsupportedOperationException(msg: String = "This is an unsupported operation!") extends Exception with FormatedException{
  def getErrorName: String = this.getClass.getName

  val code = 501
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