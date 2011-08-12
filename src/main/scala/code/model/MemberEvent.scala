package code.model

import javax.persistence._

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 12.05.11
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */

class MemberEvent {

  var qualifier: String = _
  var member: Member = _
  var event: Event = _

}

case class MemberEventPK(var event: Long, var member: String)
