package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.Location

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.07.11
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */

trait LocationDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): Location
  def read(req: Map[String, List[String]]): List[Location]
  def create(json: JValue): Location
}