package code.exceptions

import java.lang.String
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import xml.Node
import net.liftweb.json.Xml

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 08.06.11
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
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
