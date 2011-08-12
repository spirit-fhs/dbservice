package code.eclipseLink.DAO

import org.eclipse.persistence.expressions.{Expression, ExpressionBuilder}
import net.liftweb.json.JsonAST._
import scala.collection.JavaConverters._
import code.model.{Event, Appointment}
import code.{MyDB, Helper}
import javax.persistence.{EntityManagerFactory, EntityManager}
import code.exceptions.MappingException

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 20.06.11
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
 */

object ELAppointmentDAO {
  val con = MyDB.factory.getConnection().asInstanceOf[EntityManagerFactory]

  def apply() = new ELAppointmentDAO(con.createEntityManager())

  def apply(em: EntityManager) = new ELAppointmentDAO(em, true)
}

class ELAppointmentDAO(em: EntityManager, isNestedTransaction: Boolean = false) extends AbstractPersistence[Appointment](em, isNestedTransaction) with code.DAOInterfaces.AppointmentDAO {

  def delete(params: Map[String, List[String]]): JValue = {
    throw new code.exceptions.UnsupportedOperationException
  }

  def update(json: JValue, params: Map[String, List[String]]): Appointment = {
        def function(): AnyRef = {
          val appointmentDAO = ELAppointmentDAO(em)
          val oldAppointment = appointmentDAO.read(params).head
          val status: List[String] = for {JField("status", JString(arg)) <- json} yield arg
          status.head match {
            case "moved" => {
              oldAppointment.status = "moved"
              val newObj = appointmentDAO.create(json)
              oldAppointment.childAppointment = newObj
              em persist oldAppointment
              newObj
            }
            case "cancelled" => {
              oldAppointment.status = "cancelled"
              em persist oldAppointment
              oldAppointment
            }
            case _ => throw new MappingException("Can not find an appropriate status attribute!")
          }
        }
        invoke(function).asInstanceOf[Appointment]
  }

  def read(params: Map[String, List[String]]): List[Appointment] = {
    var result: List[Appointment] = Nil
    params.get("appointment_id") match {
      case Some(appointment_ids) => {
        val appointments = appointment_ids map (appointment_id => getResourceById(classOf[Appointment], appointment_id.toLong))
        if (appointments.isEmpty) {
          throw new code.exceptions.FindByCriteriaException
        }
        result = appointments
      }
      case None => {
        val eb = new ExpressionBuilder
        var expressions: List[Expression] = Nil

        if (params.get("startAppointment") != None) {
          expressions ::= eb.get("startAppointment").greaterThanEqual(Helper.pathParamToDate(params.get("startAppointment").get.head))
        } else {
          expressions ::= eb.get("endAppointment").greaterThan(eb.currentDate())
        }
        if (params.get("endAppointment") != None) {
          expressions ::= eb.get("endAppointment").lessThanEqual(Helper.pathParamToDate(params.get("endAppointment").get.head))
        }
        if (params.get("status") != None) {
          expressions ::= eb.get("status").equal(params.get("status").get.head)
        }
        if (params.get("eventType") != None) {
          expressions ::= eb.get("event").get("eventType").equal(params.get("eventType").get.head)
        }
        if (params.get("class_id") != None) {
          val degreeClassDAO = ELDegreeClassDAO(em)
          val clazzes = (degreeClassDAO.read(params))
          if (clazzes.head.classType.startsWith("Root")) {
            expressions ::= eb.get("event").anyOf("degreeClass").get("class_id").equal(clazzes.head.class_id)
          } else {
            val clazzesWithSubClazzes = clazzes flatMap (_.getAllSubClasses())
            expressions ::= concatQuerysWithOr(clazzesWithSubClazzes map (cla =>
              eb.get("event").anyOf("degreeClass").get("class_id").equal(cla.class_id)))
          }
        }
        if (params.get("fhs_id") != None) {
          expressions ::= eb.get("event").anyOf("member").get("member").get("fhs_id").equal(params.get("fhs_id").get.head)
        }
        result = getResourceByExpression(expressions)
      }
    }
    result
  }


  def create(json: JValue): Appointment = {
    def function(): AnyRef = {
      val eventDAO = ELEventDAO(em)
      val locationDAO = ELLocationDAO(em)
      val location_ids: Map[String, List[String]] = (for {
        JField("location", JArray(locationsObjects)) <- json
        JObject(location) <- locationsObjects
        JField("building", JString(building)) <- location
        JField("room", JString(room)) <- location
      } yield (building, room)).foldLeft(Map("room" -> List[String](), "building" -> List[String]())) {
        case (mapp, (building, room)) => {
          Map("room" -> (room :: mapp.get("room").get),
            "building" -> (building :: mapp.get("building").get))
        }
      }
      val event_ids = for {JField("event_id", JInt(event_id)) <- json} yield event_id
      var events = List[Event]()
      if(!event_ids.isEmpty){
        events = eventDAO.read(Map("event_id" -> List(event_ids.head.toString)))
      }
      val locations = locationDAO.read(location_ids)
      val objs: Map[String, AnyRef] = Map("event" -> (if (!events.isEmpty) events.head else null), "location" -> locations.asJava)
      val newObj = saveResource(objs, json)
      if(!event_ids.isEmpty){
        events.head.appointment.add(newObj)
      }
      locations map (location => location.appointment.add(newObj))
      em persist newObj
      newObj
    }
    invoke(function).asInstanceOf[Appointment]
  }

}