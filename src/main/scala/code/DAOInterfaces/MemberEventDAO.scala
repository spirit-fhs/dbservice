package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.MemberEvent

/**
 * This is the interface for events from a member
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */trait MemberEventDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): MemberEvent
  def read(req: Map[String, List[String]]): List[MemberEvent]
  def create(json: JValue): MemberEvent
}