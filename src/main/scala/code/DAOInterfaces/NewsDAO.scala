package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.News

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.07.11
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */

trait NewsDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): News
  def read(req: Map[String, List[String]]): List[News]
  def create(json: JValue): News
}