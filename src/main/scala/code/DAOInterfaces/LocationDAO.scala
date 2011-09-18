package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.Location

/**
 * This is the interface for locations
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
trait LocationDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): Location
  def read(req: Map[String, List[String]]): List[Location]
  def create(json: JValue): Location
}