package code.model

import java.util.Date
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._
import scala.collection.JavaConverters._
import java.text.SimpleDateFormat
import code.Helper


/**
 * This is the model of an appointment
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class Appointment extends Converter {
  var appointment_id: Long = _
  var childAppointment: Appointment = _
  var startAppointment: Date = _
  var endAppointment: Date = _
  var status: String = _
  var event: Event = _
  var location: java.util.List[Location] = _


  def getJAppointment_id: JValue = ("appointment_id" -> this.appointment_id: JValue)

  def getJStartAppointment: JValue = ("startAppointment" -> (if (this.startAppointment == null) JNull: JValue else Helper.dateToIsoStringDate(this.startAppointment): JValue))

  def getJEndAppointment: JValue = ("endAppointment" -> (if (this.endAppointment == null) JNull: JValue else Helper.dateToIsoStringDate(this.endAppointment): JValue))

  def getJStatus: JValue = ("status" -> (if (this.status == null) JNull: JValue else this.status: JValue))

  def objectToJValue: JValue = {
    val res: JValue =
      this.getJAppointment_id merge
        this.getJStartAppointment merge
        this.getJEndAppointment merge
        this.getJStatus merge
        ("childAppointment" -> {
          if (this.childAppointment == null)
            JNull: JValue
          else
            this.childAppointment.getJAppointment_id: JValue
        }) merge
          ("location" -> (this.location.asScala map (location =>
            location.getJBuilding merge
              location.getJRoom
            ))) merge
          ("event" -> {
            if (this.event == null)
              JNull: JValue
            else {
              this.event.getJEvent_id merge
                this.event.getJTitleShort merge
                this.event.getJTitleLong merge
                this.event.getJEventType merge
                this.event.getJExpireDate merge
                ("lecturer" -> (this.event.member.asScala.filter(member => member.qualifier == Member.LECTURER) map (member =>
                  member.member.getJFhs_id merge
                    member.member.getJDisplayedName merge
                    member.member.getJMemberType
                  ))) merge
                  ("degreeClass" -> (this.event.degreeClass.asScala map (degreeClass =>
                    degreeClass.getJClass_id merge
                      degreeClass.getJParent_id merge
                      degreeClass.getJTitle merge
                      degreeClass.getJClassType merge
                      degreeClass.getJMail
                    )))
            }
          })
    res
  }

  //  def objectToJValue: JValue = {
  //    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  //    val l1: JValue =
  //      ("appointment_id" -> this.appointment_id) ~
  //        ("startAppointment" -> sdf.format(this.startAppointment)) ~
  //        ("endAppointment" -> sdf.format(this.endAppointment)) ~
  //        ("status" -> this.status) ~
  //        ("childAppointment" ->  ("appointment_id" -> (if(this.childAppointment==null) JNull: JValue else this.childAppointment.appointment_id: JValue)
  //        )) ~
  //        ("location" -> (this.location.asScala map (location =>
  //          ("building" -> location.building) ~
  //            ("room" -> location.room)
  //          ))) ~
  //        ("event" -> (
  //          ("event_id" -> this.event.event_id) ~
  //            ("titleShort" -> this.event.titleShort) ~
  //            ("titleLong" -> this.event.titleLong) ~
  //            ("eventType" -> this.event.eventType) ~
  //            ("lecturer" -> (this.event.member.asScala.filter(member => member.qualifier == "lecturer") map (member =>
  //              ("fhs_id" -> member.member.fhs_id) ~
  //                ("displayedName" -> member.member.displayedName)
  //              ))) ~
  //            ("degreeClass" -> (this.event.degreeClass.asScala map (degreeClass =>
  //              ("class_id" -> degreeClass.class_id) ~
  //                ("title" -> degreeClass.title) ~
  //                ("mail" -> degreeClass.mail)
  //              )))
  //          ))
  //    l1
  //  }

  def prePersist() = {
    this.status = "ok"
  }
}

//        ("childAppointment" ->
//          (if (this.childAppointment != null)
//            Some(("appointment_id" -> this.childAppointment.appointment_id))
//          else
//            None
//            )) ~