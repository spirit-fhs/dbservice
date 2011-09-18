package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.Member

/**
 * This is the interface for member
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */trait MemberDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): Member
  def read(req: Map[String, List[String]]): List[Member]
  def create(json: JValue): Member
}