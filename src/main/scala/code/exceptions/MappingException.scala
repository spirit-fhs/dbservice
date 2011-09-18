package code.exceptions

import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import xml.Node
import net.liftweb.json.Xml

/**
 * This class is used when JSON values are not mapped by the model
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class MappingException(msg: String = "Error while convert http body to an object") extends Exception with FormatedException{
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