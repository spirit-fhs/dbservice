package code.exceptions

import net.liftweb.json.JsonAST.JValue
import xml.Node

/**
 * This trait is to be implemented when new exceptions are created
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */

trait FormatedException {
  /**
   * Get the error message as JSON
   */
  def getErrorAsJson: JValue

  /**
   * get the error message as XML
   */
  def getErrorAsXml : Node

  /**
   * Get error message
   */
  def getMessage : String

  /**
   * get the http code
   */
  def getHttpStatusCode : Int
}