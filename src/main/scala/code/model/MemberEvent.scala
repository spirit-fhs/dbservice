package code.model

/**
 * This is the model of a event from a member
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class MemberEvent {

  var qualifier: String = _
  var member: Member = _
  var event: Event = _

}

/**
 * This case class represents the primary key
 */
case class MemberEventPK(var event: Long, var member: String)
