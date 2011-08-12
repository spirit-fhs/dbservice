package code

import model._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 11.05.11
 * Time: 13:44
 * To change this template use File | Settings | File Templates.
 */

class DBConnector(val factory: DAOFactory) {

  //PUT
  def putNews(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getNewsDAO.create(json)
    ("news" -> List(res.objectToJValue))
  }
  def putNewsComment(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getNewsCommentDAO.create(json)
    ("newsComment" -> List(res.objectToJValue))
  }
  def putEvent(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getEventDAO.create(json)
    ("event" -> List(res.objectToJValue))
  }
  def putAppointment(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getAppointmentDAO.create(json)
    ("appointment" -> List(res.objectToJValue))
  }

  def putMember(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getMemberDAO.create(json)
    ("member" -> List(res.objectToJValue))
  }

  //GET
  def getNews(json: JValue, params: Map[String, List[String]]): JValue = {
    val news: Iterable[News] = factory.getNewsDAO.read(params)
    ("news" -> (news map (n => n.objectToJValue)))
  }
  def getEvent(json: JValue, params: Map[String, List[String]]): JValue = {
    val events: Iterable[Event] = factory.getEventDAO.read(params)
    ("events" -> (events map (e => e.objectToJValue)))
  }
  def getAppointment(json: JValue, params: Map[String, List[String]]): JValue = {
    val appointments: Iterable[Appointment] = factory.getAppointmentDAO.read(params)
    ("appointment" -> (appointments map (a => a.objectToJValue)))
  }
  def getDegreeClass(json: JValue, params: Map[String, List[String]]): JValue = {
    val degreeClasses: Iterable[DegreeClass] = factory.getDegreeClassDAO.read(params)
    ("degreeClass" -> (degreeClasses map (dc => dc.objectToJValue)))
  }
  def getMember(json: JValue, params: Map[String, List[String]]): JValue = {
    val member: Iterable[Member] = factory.getMemberDAO.read(params)
    ("member" -> (member map (m => m.objectToJValue)))
  }
  def getLocation(json: JValue, params: Map[String, List[String]]): JValue = {
    val location: Iterable[Location] = factory.getLocationDAO.read(params)
    ("location" -> (location map (l => l.objectToJValue)))
  }
  def getNewsComment(json: JValue, params: Map[String, List[String]]): JValue = {
    val comments: Iterable[NewsComment] = factory.getNewsCommentDAO.read(params)
    ("newsComment" -> (comments map (c => c.objectToJValue)))
  }

  //DELETE
  def deleteNews(json: JValue, params: Map[String, List[String]]) : JValue = {
    val res: JValue = factory.getNewsDAO.delete(params)
    res
  }
  def deleteNewsComment(json: JValue, params: Map[String, List[String]]) : JValue = {
    val res: JValue = factory.getNewsCommentDAO.delete(params)
    res
  }
  def deleteEvent(json: JValue, params: Map[String, List[String]]) : JValue = {
    val res: JValue = factory.getEventDAO.delete(params)
    res
  }

  def deleteMember(json: JValue, params: Map[String, List[String]]) : JValue = {
    val res: JValue = factory.getMemberDAO.delete(params)
    res
  }

  //POST
  def postNews(json: JValue, params: Map[String, List[String]]): JValue = {
    val res: News = factory.getNewsDAO.update(json, params)
    ("news" -> res.objectToJValue)
  }

  def postNewsComment(json: JValue, params: Map[String, List[String]]): JValue = {
    val res: NewsComment = factory.getNewsCommentDAO.update(json, params)
    ("newsComment" -> res.objectToJValue)
  }

  def postMember(json: JValue, params: Map[String, List[String]]): JValue = {
    val res: Member = factory.getMemberDAO.update(json, params)
    ("member" -> res.objectToJValue)
  }

  def postAppointment(json: JValue, params: Map[String, List[String]]): JValue = {
    val res: Appointment = factory.getAppointmentDAO.update(json, params)
    ("appointment" -> res.objectToJValue)
  }

}
