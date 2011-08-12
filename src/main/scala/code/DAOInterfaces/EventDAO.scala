package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.Event

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.07.11
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */

trait EventDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): Event
  def read(req: Map[String, List[String]]): List[Event]
  def create(json: JValue): Event
}