package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.Appointment

/**
 * This is the interface for appointments
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
trait AppointmentDAO{
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): Appointment
  def read(req: Map[String, List[String]]): List[Appointment]
  def create(json: JValue): Appointment
}