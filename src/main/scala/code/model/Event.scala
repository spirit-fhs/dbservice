package code.model

import java.util.Date
import net.liftweb.json.JsonDSL._
import scala.collection.JavaConverters._
import net.liftweb.json.JsonAST.{JNull, JValue}
import code.Helper

/**
 * This is the model of an event
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class Event extends Converter {
  var event_id: Long = _
  var expireDate: Date = _
  var titleShort: String = _
  var titleLong: String = _
  var member: java.util.List[MemberEvent] = _
  var appointment: java.util.List[Appointment] = _
  var degreeClass: java.util.List[DegreeClass] = _
  var eventType: String = _

  def getJEvent_id: JValue = ("event_id" -> this.event_id: JValue)

  def getJExpireDate: JValue = ("expireDate" -> (if (this.expireDate == null) JNull: JValue else Helper.dateToIsoStringDate(this.expireDate): JValue))

  def getJTitleShort: JValue = ("titleShort" -> (if (this.titleShort == null) JNull: JValue else this.titleShort: JValue))

  def getJTitleLong: JValue = ("titleLong" -> (if (this.titleLong == null) JNull: JValue else this.titleLong: JValue))

  def getJEventType: JValue = ("eventType" -> (if (this.eventType == null) JNull: JValue else this.eventType: JValue))

  def objectToJValue: JValue = {
    val res =
      this.getJEvent_id merge
        this.getJTitleShort merge
        this.getJTitleLong merge
        this.getJEventType merge
        this.getJExpireDate merge
        ("degreeClass" -> (this.degreeClass.asScala map (dClass =>
          dClass.objectToJValue
          ))) merge
        ("lecturer" -> (this.member.asScala.filter(member => member.qualifier == Member.LECTURER) map (member =>
          member.member.getJFhs_id merge
            member.member.getJDisplayedName merge
            member.member.getJMemberType
          ))) merge
        ("appointment" -> (this.appointment.asScala map (appointment =>
          appointment.getJStartAppointment merge
            appointment.getJEndAppointment merge
            appointment.getJStatus merge
            ("childAppointment" -> {
              if (appointment.childAppointment == null)
                JNull: JValue
              else
                appointment.childAppointment.getJAppointment_id: JValue
            }) merge
            ("location" -> (appointment.location.asScala map (location =>
              location.getJBuilding merge
                location.getJRoom
              )))
          )))
    res
  }

  //  def objectToJValue: JValue = {
  //    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  //    val l1 =
  //      ("event_id" -> this.event_id) ~
  //        ("titleShort" -> this.titleShort) ~
  //        ("titleLong" -> this.titleLong) ~
  //        ("eventType" -> this.eventType) ~
  //        ("expireDate" -> sdf.format(this.expireDate)) ~
  //        ("degreeClass" -> (this.degreeClass.asScala map (degreeClass =>
  //          ("class_id" -> degreeClass.class_id) ~
  //            ("title" -> degreeClass.title) ~
  //            ("mail" -> degreeClass.mail)
  //          ))) ~
  //        ("lecturer" -> (this.member.asScala.filter(member => member.qualifier == "lecturer") map (member =>
  //          ("fhs_id" -> member.member.fhs_id) ~
  //            ("displayedName" -> member.member.displayedName)
  //          )))
  //    l1
  //  }
}