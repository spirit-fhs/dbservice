package code.model

import net.liftweb.json.JsonAST.JValue

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 05.06.11
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */

trait Converter {
  def objectToJValue : JValue
}