package code.eclipseLink.DAO

import net.liftweb.json.JsonAST._
import org.eclipse.persistence.expressions.{Expression, ExpressionBuilder}
import code.MyDB
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import javax.persistence.{EntityManagerFactory, EntityManager}
import code.model.{DegreeClass, Member}
import code.exceptions.{UnknownErrorException, FindByCriteriaException, PermissionException}
import net.liftweb.json.JsonDSL._

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 12.06.11
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 */

object ELMemberDAO {
  val con = MyDB.factory.getConnection().asInstanceOf[EntityManagerFactory]

  def apply() = new ELMemberDAO(con.createEntityManager())

  def apply(em: EntityManager) = new ELMemberDAO(em, true)
}

class ELMemberDAO(em: EntityManager, isNestedTransaction: Boolean = false) extends AbstractPersistence[Member](em, isNestedTransaction) with code.DAOInterfaces.MemberDAO {

  def read(params: Map[String, List[String]]): List[Member] = {
    def function(): AnyRef = {
      val eb = new ExpressionBuilder
      var expressions: List[Expression] = Nil
      params.keys foreach {
        key => {
          key match {
            case "fhs_id" => return params.get(key).get map (fhs_id => getResourceById(classOf[Member], fhs_id))
            case "class_id" => expressions ::= eb.anyOf("degreeClass").get(key).equal(params.get(key).get.head.toInt)
          }
        }
      }
      getResourceByExpression(expressions)
    }
    invoke(function).asInstanceOf[List[Member]]
  }

  def create(json: JValue): Member = {
    def function(): AnyRef = {
      val degreeClassDAO = ELDegreeClassDAO(em)
      val clazz_ids: List[String] = for {JField("class_id", JInt(class_id)) <- json} yield class_id.toString
      var clazzes: List[DegreeClass] = Nil
      var map: Map[String, AnyRef] = Map()
      if (!clazz_ids.isEmpty) {
        clazzes = degreeClassDAO.read(Map("class_id" -> clazz_ids))
        map += ("degreeClass" -> clazzes.asJava)
      }
      val newObj = saveResource(map, json)
      clazzes.foreach(clazz => if (clazz.member.contains(newObj)) clazz.member.add(newObj))
      em persist newObj
      newObj
    }
    invoke(function).asInstanceOf[Member]
  }

  def delete(params: Map[String, List[String]]): JValue = {
    def function(): AnyRef = {
      params.get("fhs_id") match {
        case Some(fhs_ids) => {
          val member = getResourceById(classOf[Member], fhs_ids.head)
          val newsCommentDAO = ELNewsCommentDAO(em)
          val newsDAO = ELNewsDAO(em)
          val newsComments = newsCommentDAO.read(Map("owner" -> List(member.fhs_id)))
          newsComments.foreach(comment => newsCommentDAO.delete(Map("comment_id" -> List(comment.comment_id.toString))))
          val news = newsDAO.read(Map("owner" -> List(member.fhs_id), "creationDate" -> List("19000101000000")))
          news.foreach(news => newsDAO.delete(Map("news_id" -> List(news.news_id.toString))))
          em.flush()
          em.remove(member)
        }
        case None => throw new code.exceptions.FindByCriteriaException
      }
      null
    }
    invoke(function)
    ("info" -> ("message" -> "Successfull deleted"))
  }

  def update(json: JValue, params: Map[String, List[String]]): Member = {
    def function(): AnyRef = {
      val memberDAO = ELMemberDAO(em)
      val degreeClassDAO = ELDegreeClassDAO(em)
      val member = memberDAO.read(params).head
      val oldClazzes = member.degreeClass
      val fhs_ids = params.get("fhs_id")
      if (!fhs_ids.isEmpty) {
        fhs_ids.get.head match {
          case fhs_id: String => {
            if (member.fhs_id == fhs_id) {
              val currentClass_ids: List[String] = for {JField("class_id", JInt(class_id)) <- json} yield class_id.toString
              val currentClasses: List[DegreeClass] = degreeClassDAO.read(Map("class_id" -> currentClass_ids))
              oldClazzes.foreach(oldClazz => if (!currentClasses.contains(oldClazz)) oldClazz.member.remove(member))
              val objs: Map[String, AnyRef] = Map("degreeClass" -> currentClasses.asJava)
              val newObj = saveResource(objs, json, member)
              currentClasses.foreach(currentClass => if (!currentClass.member.contains(newObj)) currentClass.member.add(newObj))
              em persist newObj
              newObj
            } else {
              println("gesendet: "+ fhs_id)
              println("alte: "+ member.fhs_id)
              throw new PermissionException
            }
          }
          case _ => {
            println("unknown error")
            throw new UnknownErrorException
          }
        }
      } else throw new FindByCriteriaException
    }
    invoke(function).asInstanceOf[Member]
  }
}