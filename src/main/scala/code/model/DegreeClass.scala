package code.model

import net.liftweb.json.JsonDSL._
import scala.collection.JavaConverters._
import net.liftweb.json.JsonAST.{JNull, JValue}
import ch.qos.logback.core.joran.conditional.ElseAction
import util.parsing.json.JSON
import net.liftweb.json._

/**
 * This is the model of a class or group
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
class DegreeClass extends Converter {
  var class_id: Long = _
  var title: String = _
  var mail: String = _
  var classType: String = _
  var subClasses: java.util.List[DegreeClass] = _
  var parent: DegreeClass = _
  var event: java.util.List[Event] = _
  var member: java.util.List[Member] = _
  var news: java.util.List[News] = _

  def getJClass_id: JValue = ("class_id" -> this.class_id: JValue)

  def getJTitle: JValue = ("title" -> (if (this.title == null) JNull: JValue else this.title: JValue))

  def getJMail: JValue = ("mail" -> (if (this.mail == null) JNull: JValue else this.mail: JValue))

  def getJClassType: JValue = ("classType" -> (if (this.classType == null) JNull: JValue else this.classType: JValue))

  def getJParent_id: JValue = ("parent" -> (if (this.parent == null) {
    JNull: JValue
  }
  else {
    (("class_id") -> this.parent.class_id merge
      ("title") -> this.parent.title): JValue
  }
    ))

  def objectToJValue: JValue = {
    val res =
      this.getJClass_id merge
        this.getJParent_id merge
        this.getJTitle merge
        this.getJClassType merge
        this.getJMail merge
        this.getJAllSubClasses
    res
  }

  def getAllSubClasses(): List[DegreeClass] = {
    def recursiv(clazzes: List[DegreeClass], acc: List[DegreeClass] = Nil): List[DegreeClass] = {
      if (clazzes.isEmpty) {
        return acc
      }
      else {
        val childs = clazzes.flatMap(node => node.subClasses.asScala.toList)
        recursiv(childs, clazzes ++ acc)
      }
    }
    recursiv(List(this))
  }


  def getJAllSubClasses(): JValue = {
    var circle: List[DegreeClass] = Nil
    def recursiv(clazzes: List[DegreeClass], acc: JValue = JNull): JValue = {
      if (clazzes.isEmpty) {
        return acc
      }
      else {
        val childs = clazzes.flatMap(node => node.subClasses.asScala.toList)
        recursiv(childs, acc merge (
          "subClasses" -> (childs map (child => {
            if (!circle.contains(child)) {
              circle ::= child
              Some(child.getJClass_id merge
                child.getJParent_id merge
                child.getJTitle merge
                child.getJClassType merge
                child.getJMail merge
                recursiv(List(child)))
            } else {
              None
            }
          }))))
      }
    }
    recursiv(List(this))
  }

  //  def getJAllSubClasses: JValue = ("subClasses") -> getAllSubClasses().filterNot(_.class_id == this.class_id).map(clazz => {
  //    clazz.getJClass_id merge
  //      clazz.getJParent_id merge
  //      clazz.getJTitle merge
  //      clazz.getJClassType merge
  //      clazz.getJMail
  //  })

  //  def objectToJValue: JValue = {
  //    val l1 =
  //      ("class_id" -> this.class_id) ~
  //        ("title" -> this.title) ~
  //        ("classType" -> this.classType) ~
  //        ("mail" -> (if(this.mail!=null) Some(this.mail) else None)) ~
  //        ("subClasses" -> (this.subClasses.asScala map (degreeClass =>
  //          ("class_id" -> degreeClass.class_id) ~
  //            ("title" -> degreeClass.title) ~
  //            ("classType" -> degreeClass.classType) ~
  //            ("mail" -> (if(degreeClass.mail!=null) Some(degreeClass.mail) else None))
  //          ))) ~
  //        ("member" -> (this.member.asScala map (member =>
  //          ("fhs_id" -> member.fhs_id) ~
  //          ("displayedName" -> member.displayedName)
  //          )))
  //    l1
  //  }
}
