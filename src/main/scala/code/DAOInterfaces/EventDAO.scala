package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.Event

/**
 * This is the interface for events
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */trait EventDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): Event
  def read(req: Map[String, List[String]]): List[Event]
  def create(json: JValue): Event
}