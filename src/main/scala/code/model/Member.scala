package code.model

import net.liftweb.json.JsonDSL._
import scala.collection.JavaConverters._
import net.liftweb.json.JsonAST.{JNull, JValue}

/**
 * This is the model of a member
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
object Member {
  val STUDENT: String = "student"
  val LECTURER: String = "lecturer"
}


class Member extends Converter {

  var fhs_id: String = _
  var displayedName: String = _
  var comment: java.util.List[NewsComment] = _
  var event: java.util.List[MemberEvent] = _
  var degreeClass: java.util.List[DegreeClass] = _
  var news: java.util.List[News] = _
  var memberType: String = _

  def getJFhs_id: JValue = ("fhs_id" -> (if (this.fhs_id == null) JNull: JValue else this.fhs_id: JValue))

  def getJDisplayedName: JValue = ("displayedName" -> (if (this.displayedName == null) JNull: JValue else this.displayedName: JValue))

  def getJMemberType: JValue = ("memberType" -> (if (this.memberType == null) JNull: JValue else this.memberType: JValue))

  def objectToJValue: JValue = {
    val res =
      this.getJFhs_id merge
        this.getJDisplayedName merge
        this.getJMemberType
    res
  }

}

//If you use annotations sees it this way

//@Entity
//@Table(name = "Member")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 20)
//@DiscriminatorValue("Student")
//class Member {
//
//  @Id
//  @Column(nullable = false, length = 30)
//  var fhsId: String = _
//  @Column(length = 50)
//  var displayedName: String = _
//  @OneToMany(cascade = Array(CascadeType.ALL), mappedBy = "owner")
//  var newsComment: java.util.List[NewsComment] = _
//
//}