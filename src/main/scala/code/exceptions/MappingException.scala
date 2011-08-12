package code.exceptions

import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import xml.Node
import net.liftweb.json.Xml

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 11.06.11
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
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