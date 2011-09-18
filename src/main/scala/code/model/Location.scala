package code.model

import net.liftweb.json.JsonAST.{JNull, JValue}
import net.liftweb.json.JsonDSL._


/**
 * This is the model of a location
 *
 * @version 1.0
 * @author Benjamin Lüdicke
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

/**
 * This case class represents the primary key of a location
 */
case class LocationPK(var building: String, var room: String)