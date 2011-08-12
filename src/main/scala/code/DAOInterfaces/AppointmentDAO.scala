package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.Appointment

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.07.11
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */

trait AppointmentDAO{
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): Appointment
  def read(req: Map[String, List[String]]): List[Appointment]
  def create(json: JValue): Appointment
}