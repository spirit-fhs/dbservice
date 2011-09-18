package code

import model._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

/**
 * This class execute the http request with a appropriate DAOFactory
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class DBConnector(val factory: DAOFactory) {

  //PUT
  /**
   * Create a news in the database
   *
   * @return returns the created news as JSON
   */
  def putNews(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getNewsDAO.create(json)
    ("news" -> List(res.objectToJValue))
  }
  /**
   * Create a comment in the database
   *
   * @return returns the created comment as JSON
   */
  def putNewsComment(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getNewsCommentDAO.create(json)
    ("newsComment" -> List(res.objectToJValue))
  }
  /**
   * Create a event in the database
   *
   * @return returns the created event as JSON
   */
  def putEvent(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getEventDAO.create(json)
    ("event" -> List(res.objectToJValue))
  }
  /**
   * Create a appointment in the database
   *
   * @return returns the created appointment as JSON
   */
  def putAppointment(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getAppointmentDAO.create(json)
    ("appointment" -> List(res.objectToJValue))
  }
  /**
   * Create a member in the database
   *
   * @return returns the created member as JSON
   */
  def putMember(json: JValue, params: Map[String, List[String]]): JValue = {
    val res = factory.getMemberDAO.create(json)
    ("member" -> List(res.objectToJValue))
  }

  //GET
  /**
   * Get some news, considers the params
   */
  def getNews(json: JValue, params: Map[String, List[String]]): JValue = {
    val news: Iterable[News] = factory.getNewsDAO.read(params)
    ("news" -> (news map (n => n.objectToJValue)))
  }
  /**
   * Get some events, considers the params
   */
  def getEvent(json: JValue, params: Map[String, List[String]]): JValue = {
    val events: Iterable[Event] = factory.getEventDAO.read(params)
    ("events" -> (events map (e => e.objectToJValue)))
  }
  /**
   * Get some appointments, considers the params
   */
  def getAppointment(json: JValue, params: Map[String, List[String]]): JValue = {
    val appointments: Iterable[Appointment] = factory.getAppointmentDAO.read(params)
    ("appointment" -> (appointments map (a => a.objectToJValue)))
  }
  /**
   * Get some classes, considers the params
   */
  def getDegreeClass(json: JValue, params: Map[String, List[String]]): JValue = {
    val degreeClasses: Iterable[DegreeClass] = factory.getDegreeClassDAO.read(params)
    ("degreeClass" -> (degreeClasses map (dc => dc.objectToJValue)))
  }
  /**
   * Get some member, considers the params
   */
  def getMember(json: JValue, params: Map[String, List[String]]): JValue = {
    val member: Iterable[Member] = factory.getMemberDAO.read(params)
    ("member" -> (member map (m => m.objectToJValue)))
  }
  /**
   * Get some locations, considers the params
   */
  def getLocation(json: JValue, params: Map[String, List[String]]): JValue = {
    val location: Iterable[Location] = factory.getLocationDAO.read(params)
    ("location" -> (location map (l => l.objectToJValue)))
  }
  /**
   * Get some comments, considers the params
   */
  def getNewsComment(json: JValue, params: Map[String, List[String]]): JValue = {
    val comments: Iterable[NewsComment] = factory.getNewsCommentDAO.read(params)
    ("newsComment" -> (comments map (c => c.objectToJValue)))
  }

  //DELETE
  /**
   * Delete a news
   */
  def deleteNews(json: JValue, params: Map[String, List[String]]) : JValue = {
    val res: JValue = factory.getNewsDAO.delete(params)
    res
  }
  /**
   * Delete a comment
   */
  def deleteNewsComment(json: JValue, params: Map[String, List[String]]) : JValue = {
    val res: JValue = factory.getNewsCommentDAO.delete(params)
    res
  }
  /**
   * Delete a event
   */
  def deleteEvent(json: JValue, params: Map[String, List[String]]) : JValue = {
    val res: JValue = factory.getEventDAO.delete(params)
    res
  }
  /**
   * Delete a member
   */
  def deleteMember(json: JValue, params: Map[String, List[String]]) : JValue = {
    val res: JValue = factory.getMemberDAO.delete(params)
    res
  }

  //POST
  /**
   * Change the content of a news
   */
  def postNews(json: JValue, params: Map[String, List[String]]): JValue = {
    val res: News = factory.getNewsDAO.update(json, params)
    ("news" -> res.objectToJValue)
  }
  /**
   * Change the content of a comment
   */
  def postNewsComment(json: JValue, params: Map[String, List[String]]): JValue = {
    val res: NewsComment = factory.getNewsCommentDAO.update(json, params)
    ("newsComment" -> res.objectToJValue)
  }
  /**
   * Change the properties of a member
   */
  def postMember(json: JValue, params: Map[String, List[String]]): JValue = {
    val res: Member = factory.getMemberDAO.update(json, params)
    ("member" -> res.objectToJValue)
  }
  /**
   * Change the properties of a appointment
   */
  def postAppointment(json: JValue, params: Map[String, List[String]]): JValue = {
    val res: Appointment = factory.getAppointmentDAO.update(json, params)
    ("appointment" -> res.objectToJValue)
  }

}
