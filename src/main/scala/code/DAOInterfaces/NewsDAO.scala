package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.News

/**
 * This is the interface for news
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
trait NewsDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): News
  def read(req: Map[String, List[String]]): List[News]
  def create(json: JValue): News
}