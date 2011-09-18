package code.eclipseLink.DAO

import org.eclipse.persistence.expressions.{Expression, ExpressionBuilder}
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._
import code.exceptions.{UnknownErrorException, PermissionException, FindByCriteriaException}
import code.model.{Member, News, DegreeClass}
import code.{MyDB, Helper}
import javax.persistence.{EntityManagerFactory, EntityManager}

/**
 * This class represents a DAO for news
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
object ELNewsDAO {
  val con = MyDB.factory.getConnection().asInstanceOf[EntityManagerFactory]

  def apply() = new ELNewsDAO(con.createEntityManager())

  def apply(em: EntityManager) = new ELNewsDAO(em)
}

class ELNewsDAO(em: EntityManager) extends AbstractPersistence[News](em) with code.DAOInterfaces.NewsDAO {

  def create(json: JValue): News = {
    def function(): AnyRef = {
      val memberDAO = ELMemberDAO(em)
      val degreeClassDAO = ELDegreeClassDAO(em)

      val member_ids: List[String] = for {JField("fhs_id", JString(fhs_id)) <- json} yield fhs_id
      val members: List[Member] = memberDAO.read(Map("fhs_id" -> member_ids))

      val clazz_ids: List[String] = for {JField("class_id", JInt(class_id)) <- json} yield class_id.toString
      val clazzes: List[DegreeClass] = degreeClassDAO.read(Map("class_id" -> clazz_ids))

      val objs: Map[String, AnyRef] = Map(("owner" -> members.head), ("degreeClass" -> clazzes.asJava))

      val newObj = saveResource(objs, json)
      members.head.news.add(newObj)
      clazzes.map(clazz => clazz.news.add(newObj))
      em persist newObj
      newObj
    }
    invoke(function).asInstanceOf[News]
  }

  def read(params: Map[String, List[String]]): List[News] = {
    def function(): AnyRef = {
      val eb = new ExpressionBuilder
      var expressions: List[Expression] = Nil
      params.keySet foreach {
        key => {
          key match {
            case "news_id" => return params.get(key).get map (news_id => getResourceById(classOf[News], news_id.toLong))
            case "owner" => expressions ::= eb.get(key).get("fhs_id").equal((params.get(key)).get.head)
            case "class_id" => {
              val degreeClassDAO = ELDegreeClassDAO(em)
              val clazzes = degreeClassDAO.read(params) flatMap (degreeClassDAO.readWithSubClasses(_))
              expressions ::= concatQuerysWithOr(clazzes map (clazz =>
                eb.anyOf("degreeClass").get(key).equal(clazz.class_id)))
            }
            case "creationDate" => expressions ::= {
              eb.get(key).greaterThanEqual(Helper.pathParamToDate(params.get(key).get.head))
            }
          }
        }
      }
      if (!params.contains("creationDate"))
        expressions ::= eb.get("expireDate").greaterThan(eb.currentDate())
      addOrderBy(eb.get("lastModified").descending())
      getResourceByExpression(expressions)
    }
    invoke(function).asInstanceOf[List[News]]
  }

  def update(json: JValue, params: Map[String, List[String]]): News = {
    def function(): AnyRef = {
      val degreeClassDAO = ELDegreeClassDAO(em)
      val newsDAO = ELNewsDAO(em)
      val news = newsDAO.read(params).head
      val owner: List[JValue] = for {
        JField("fhs_id", arg) <- json
      } yield arg
      if (!owner.isEmpty) {
        owner.head match {
          case JString(fhs_id) => {
            if (news.owner.fhs_id == fhs_id) {
              val oldClazzes = news.degreeClass
              val clazz_ids: List[String] = for {
                JField("class_id", JInt(class_id)) <- json
              } yield class_id.toString
              val currentClasses: List[DegreeClass] = degreeClassDAO.read(Map("class_id" -> clazz_ids))
              oldClazzes.foreach(oldClazz => if(!currentClasses.contains(oldClazz)) oldClazz.news.remove(news))
              val objs: Map[String, AnyRef] = Map("degreeClass" -> currentClasses.asJava)
              val newObj = saveResource(objs, json, news)
              currentClasses.foreach(currentClass => if(!currentClass.news.contains(newObj)) currentClass.news.add(newObj))
              em persist newObj
              newObj
            } else throw new PermissionException
          }
          case _ => throw new UnknownErrorException
        }
      } else throw new FindByCriteriaException
    }
    invoke(function).asInstanceOf[News]
  }

  def delete(params: Map[String, List[String]]): JValue = {
    def function(): AnyRef = {
      params.get("news_id") match {
        case Some(news_ids) => {
          val news = getResourceById(classOf[News], news_ids.head.toLong)
          if (news == null)
            throw new code.exceptions.FindByCriteriaException
          em.remove(news)
          em.refresh(news.owner)
        }
        case None => {
          throw new code.exceptions.FindByCriteriaException
        }
      }
      null
    }
    invoke(function)
    ("info" -> ("message" -> "Successfull deleted"))
  }

}