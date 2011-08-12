package code.model

import net.liftweb.json.JsonAST.{JNull, JValue}
import net.liftweb.json.JsonDSL._
import code.Helper

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 19.05.11
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */

class Location {
  var building: String = _
  var room: String = _
  var appointment: java.util.List[Appointment] = _

  def getJBuilding : JValue = ("building" -> (if(this.building==null) JNull: JValue else this.building: JValue))
  def getJRoom : JValue = ("room" -> (if(this.room==null) JNull: JValue else this.room: JValue))

  def objectToJValue: JValue = {
    val res =
      this.getJBuilding merge
      this.getJRoom
    res
  }

}

case class LocationPK(var building: String, var room: String)