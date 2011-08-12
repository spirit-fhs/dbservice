package code.exceptions

import net.liftweb.json.JsonAST.JValue
import xml.Node

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 13.06.11
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */

trait FormatedException {
  def getErrorAsJson: JValue
  def getErrorAsXml : Node
  def getMessage : String
  def getHttpStatusCode : Int
}