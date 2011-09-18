package code.eclipseLink.DAO

import code.model.MemberEvent
import net.liftweb.json.JsonAST._
import code.MyDB
import javax.persistence.{EntityManagerFactory, EntityManager}
import net.liftweb.json.JsonAST.{JValue, JInt, JField, JString}

/**
 * This class represents a DAO for events from a member
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
object ELMemberEventDAO {
  val con = MyDB.factory.getConnection().asInstanceOf[EntityManagerFactory]

  def apply() = new ELMemberEventDAO(con.createEntityManager())

  def apply(em: EntityManager) = new ELMemberEventDAO(em)
}

class ELMemberEventDAO(em: EntityManager) extends AbstractPersistence[MemberEvent](em) with code.DAOInterfaces.MemberEventDAO {

  def delete(params: Map[String, List[String]]): JValue = JNull

  def update(json: JValue, params: Map[String, List[String]]): MemberEvent = null

  def read(params: Map[String, List[String]]): List[MemberEvent] = null

  def create(json: JValue): MemberEvent = {
    def function(): AnyRef = {
      val memberDAO = ELMemberDAO(em)
      val eventDAO = ELEventDAO(em)
      val member_ids = for {JField("fhs_id", JString(fhs_id)) <- json} yield fhs_id
      val members = memberDAO.read(Map("fhs_id" -> member_ids))

      val event_ids = for {JField("event_id", JInt(event_id)) <- json} yield event_id.toString
      val events = eventDAO.read(Map("event_id" -> event_ids))

      val objs: Map[String, AnyRef] = Map("event" -> events.head, "member" -> members.head)
      val newObj = saveResource(objs, json)
      members.head.event.add(newObj)
      events.head.member.add(newObj)
      em persist newObj
      newObj
    }
    invoke(function).asInstanceOf[MemberEvent]
  }
}