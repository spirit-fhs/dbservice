package code.eclipseLink.DAO

import org.eclipse.persistence.expressions.{Expression, ExpressionBuilder}
import java.text.SimpleDateFormat
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._
import scala.collection.JavaConverters._
import collection.immutable.Map
import code.model.{Appointment, MemberEvent, Member, Event}
import javax.persistence.{EntityManagerFactory, EntityManager}
import code.{Helper, MyDB}

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 12.06.11
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */

object ELEventDAO {
  val con = MyDB.factory.getConnection().asInstanceOf[EntityManagerFactory]

  def apply() = new ELEventDAO(con.createEntityManager())

  def apply(em: EntityManager) = new ELEventDAO(em, true)
}

class ELEventDAO(em: EntityManager, isNestedTransaction: Boolean = false) extends AbstractPersistence[Event](em, isNestedTransaction) with code.DAOInterfaces.EventDAO {

  def read(params: Map[String, List[String]]): List[Event] = {
    def function(): AnyRef = {
      val eb = new ExpressionBuilder
      var expressions: List[Expression] = Nil
      params.keys foreach {
        key => {
          key match {
            case "event_id" => return params.get(key).get map (event_id => getResourceById(classOf[Event], event_id.toLong))
            case "startAppointment" => expressions ::= eb.anyOf("appointment").get(key).greaterThanEqual(Helper.pathParamToDate(params.get(key).get.head))
            case "endAppointment" => expressions ::= eb.anyOf("appointment").get(key).lessThanEqual(Helper.pathParamToDate(params.get(key).get.head))
            case "eventType" => expressions ::= eb.get(key).equal(params.get(key).get.head)
            case "fhs_id" => expressions ::= eb.anyOf("member").get("member").get(key).equal(params.get(key).get.head)
          }
        }
      }
      getResourceByExpression(expressions)
    }
    invoke(function).asInstanceOf[List[Event]]
  }


  def update(json: JValue, params: Map[String, List[String]]): Event = null

  def create(json: JValue): Event = {
    def function(): AnyRef = {
      val degreeClassDAO = ELDegreeClassDAO(em)
      val appointmentDAO = ELAppointmentDAO(em)
      val memberDAO = ELMemberDAO(em)
      val clazz_ids = for {JField("class_id", JInt(class_id)) <- json} yield class_id.toString
      val clazzes = degreeClassDAO.read(Map("class_id" -> clazz_ids))
      val clazzesWithChilds = clazzes flatMap (_.getAllSubClasses())

      val member_ids = for {JField("fhs_id", JString(fhs_id)) <- json} yield fhs_id
      val members = memberDAO.read(Map("fhs_id" -> member_ids))

      val memberEvents = members map (member => {
        val memberEvent = new MemberEvent
        memberEvent.member = member
        memberEvent.qualifier = Member.LECTURER
        member.event.add(memberEvent)
        memberEvent
      })

      val appointments: List[Appointment] = (for {
        JField("appointment", JArray(jObjects)) <- json
        JObject(appointment) <- jObjects
      } yield appointment) map (appointment => {
        appointmentDAO.create(appointment)
      })

      val objs: Map[String, AnyRef] = Map("appointment" -> appointments.asJava, "degreeClass" -> clazzesWithChilds.asJava, "member" -> memberEvents.asJava)
      val newObj = saveResource(objs, json)
      clazzesWithChilds foreach (child => child.event.add(newObj))
      appointments foreach (appointment => appointment.event = newObj)
      memberEvents foreach (memberEvent => memberEvent.event = newObj)
      em persist newObj
      newObj
    }
    invoke(function).asInstanceOf[Event]
  }

  def delete(params: Map[String, List[String]]): JValue = {
    def function(): AnyRef = {
      params.get("event_id") match {
        case Some(event_ids) => {
          val event = getResourceById(classOf[Event], event_ids.head.toLong)
          if (event == null)
            throw new code.exceptions.FindByCriteriaException
          em.remove(event)
        }
        case None => {
          throw new code.exceptions.FindByCriteriaException
        }
      }
      null
    }
    invoke(function)
    ("info" -> ("message" -> "Successfull deleted"))
  }


}