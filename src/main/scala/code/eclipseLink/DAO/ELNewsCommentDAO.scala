package code.eclipseLink.DAO

import org.eclipse.persistence.expressions.{Expression, ExpressionBuilder}
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._
import scala.Boolean
import code.MyDB
import javax.persistence.{EntityManagerFactory, EntityManager}
import code.model.{DegreeClass, Member, News, NewsComment}
import code.exceptions.{FindByCriteriaException, UnknownErrorException, PermissionException}
import ch.qos.logback.core.joran.conditional.ElseAction

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 12.06.11
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */

object ELNewsCommentDAO {
  val con = MyDB.factory.getConnection().asInstanceOf[EntityManagerFactory]

  def apply() = new ELNewsCommentDAO(con.createEntityManager())

  def apply(em: EntityManager) = new ELNewsCommentDAO(em, true)
}

class ELNewsCommentDAO(em: EntityManager, isNestedTransaction: Boolean = false) extends AbstractPersistence[NewsComment](em, isNestedTransaction) with code.DAOInterfaces.NewsCommentDAO {

  def create(json: JValue): NewsComment = {
    def function(): AnyRef = {
      val memberDAO = ELMemberDAO(em)
      val newsDAO = ELNewsDAO(em)
      val member_ids = for {JField("fhs_id", JString(fhs_id)) <- json} yield fhs_id
      val members: List[Member] = memberDAO.read(Map("fhs_id" -> member_ids))

      val news_ids = for {JField("news_id", JInt(news_id)) <- json} yield news_id.toString
      val news: List[News] = newsDAO.read(Map("news_id" -> news_ids))

      val objs: Map[String, AnyRef] = Map(("owner" -> members.head), ("news" -> news.head))

      val newObj = saveResource(objs, json)
      news.head.comment.add(newObj)
      members.head.comment.add(newObj)
      em persist newObj
      newObj
    }
    invoke(function).asInstanceOf[NewsComment]
  }

  def read(params: Map[String, List[String]]): List[NewsComment] = {
    def function(): AnyRef = {
      val eb = new ExpressionBuilder
      var expressions: List[Expression] = Nil
      params.keys foreach {
        key => {
          key match {
            case "comment_id" => return params.get(key).get map (comment_id => getResourceById(classOf[NewsComment], comment_id.toLong))
            case "owner" => expressions ::= eb.get(key).get("fhs_id").equal(params.get(key).get.head)
            case "news_id" => expressions ::= eb.get("news").get(key).equal(params.get(key).get.head)
          }
        }
      }
      addOrderBy(eb.get("creationDate").descending())
      getResourceByExpression(expressions)
    }
    invoke(function).asInstanceOf[List[NewsComment]]
  }

  def delete(params: Map[String, List[String]]): JValue = {
    def function(): AnyRef = {
      params.get("comment_id") match {
        case Some(comment_ids) => {
          val comment = getResourceById(classOf[NewsComment], comment_ids.head.toLong)
          em.remove(comment)
          em.refresh(comment.news)
          em.refresh(comment.owner)
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

  def update(json: JValue, params: Map[String, List[String]]): NewsComment = {
    def function(): AnyRef = {
      val newsCommentDAO = ELNewsCommentDAO(em)
      val newsComment = newsCommentDAO.read(params).head
      val newsDAO = ELNewsDAO(em)
      val owner: List[JValue] = for {
        JField("fhs_id", arg) <- json
      } yield arg
      if (!owner.isEmpty) {
        owner.head match {
          case JString(fhs_id) => {
            if (newsComment.owner.fhs_id == fhs_id) {
              val news_ids: List[String] = for{JField("news_id", JInt(news_id)) <- json} yield news_id.toString
              val currentNews: News = newsDAO.read(Map("news_id" -> news_ids)).head
              val oldNews = newsComment.news
              if (currentNews != oldNews)
                oldNews.comment.remove(newsComment)
              val objs: Map[String, AnyRef] = Map("news" -> currentNews)
              val newObj = saveResource(objs, json, newsComment)
              if (!currentNews.comment.contains(newObj))
                currentNews.comment.add(newObj)
              em persist newObj
              newObj
            } else throw new PermissionException
          }
          case _ => throw new UnknownErrorException
        }
      } else throw new FindByCriteriaException
    }
    invoke(function).asInstanceOf[NewsComment]
  }
}