package code.exceptions

import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import xml.Node
import net.liftweb.json.Xml

/**
 * This class is used when the member has no permissions to change values in entities
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class PermissionException (msg: String = "No permission!") extends Exception with FormatedException{
  def getErrorName: String = this.getClass.getName

  val code = 403
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