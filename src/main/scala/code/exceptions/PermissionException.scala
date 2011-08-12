package code.exceptions

import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import xml.Node
import net.liftweb.json.Xml

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 15.07.11
 * Time: 22:25
 * To change this template use File | Settings | File Templates.
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