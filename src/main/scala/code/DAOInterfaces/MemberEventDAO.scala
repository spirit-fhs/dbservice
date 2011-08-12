package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.MemberEvent

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.07.11
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */

trait MemberEventDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): MemberEvent
  def read(req: Map[String, List[String]]): List[MemberEvent]
  def create(json: JValue): MemberEvent
}