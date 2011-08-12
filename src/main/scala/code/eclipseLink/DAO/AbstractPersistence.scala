package code.eclipseLink.DAO

import org.eclipse.persistence.expressions.Expression
import scala.collection.JavaConversions._
import org.eclipse.persistence.jpa.JpaHelper
import code.exceptions.{MappingException, FindByCriteriaException}
import net.liftweb.json.JsonAST._
import code.Helper
import org.eclipse.persistence.queries.ReadAllQuery
import javax.persistence.{EntityTransaction, Query, EntityManager}
import java.lang.NumberFormatException

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.06.11
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */


abstract class AbstractPersistence[T <: AnyRef : ClassManifest](em: EntityManager, isNestedTransaction: Boolean) {

  private[this] val readAll = new ReadAllQuery(classManifest[T].erasure)

  def concatQuerysWithAnd(exp: List[Expression]): Expression = {
    val complex: Expression = null
    exp.foldLeft(complex) {
      case (r, c) if (r == null && c != null) => c
      case (r, c) if (r != null && c != null) => r.and(c)
      case (r, c) if (r == null || c == null) => r
    }
  }

  def concatQuerysWithOr(exp: List[Expression]): Expression = {
    val complex: Expression = null
    exp.foldLeft(complex) {
      case (r, c) if (r == null && c != null) => c
      case (r, c) if (r != null && c != null) => r.or(c)
      case (r, c) if (r == null || c == null) => r
    }
  }

  def getResourceById[T <: AnyRef](clazz: Class[T], pk: Any): T = {
    val res = em.find(clazz, pk)
    if (res == null)
      throw new FindByCriteriaException
    res
  }

  def addOrderBy(orderExpression: Expression): Unit = {
    readAll.addOrdering(orderExpression)
  }

  def getResourceByExpression(expressions: List[Expression]): List[T] = {
    val jpaEm = JpaHelper.getEntityManager(em)
    val exp: Expression = concatQuerysWithAnd(expressions)
    readAll.setSelectionCriteria(exp)
    val query: Query = jpaEm.createQuery(readAll)
    val result: List[T] = query.getResultList.toList.asInstanceOf[List[T]]
    if (result.isEmpty)
      throw new FindByCriteriaException
    result
  }

  def saveResource(objs: Map[String, AnyRef], json: JValue,
                  newObj: T = classManifest[T].erasure.newInstance.asInstanceOf[T]): T = {
    val methods = classManifest[T].erasure.getMethods filter (_.getName.endsWith("_$eq"))

    methods foreach (method => {
      val methodName: String = method.getName.dropRight(4)
      if (objs.contains(methodName)) {
        val obj = objs.get(methodName)
        obj match {
          case Some(instance) => {
            method.invoke(newObj, instance)
          }
          case None =>
        }
      }
      else {
        val fields: List[JValue] = for {JField(attribute, arg) <- json if (attribute == methodName)} yield arg
        if (!fields.isEmpty) {
          fields.head match {
            case JString(jv) => {
              if (method.getParameterTypes.head == classOf[java.util.Date]) {
                method.invoke(newObj, Helper.isoStringDateToDate(jv))
              } else {
                method.invoke(newObj, jv)
              }
            }
            case JInt(jv) => method.invoke(newObj, jv)
            case JBool(jv) => method.invoke(newObj, jv.asInstanceOf[AnyRef])
            case JObject(_) =>
            case JArray(_) =>
            case JDouble(_) =>
            case JField(_,_) =>
            case JNull =>
            case _ => throw new MappingException("There is an unknown type in the http body!")
          }
        }
      }
    })
    newObj
  }

  def invoke (f:()=> AnyRef): AnyRef = {
    var tx: EntityTransaction = null
    if (!isNestedTransaction) tx = em.getTransaction
    try {
      if (!isNestedTransaction) tx.begin
      val newObj = f()
      if (!isNestedTransaction) tx.commit
      newObj
    } catch {
      case ex: code.exceptions.FormatedException => {
        if (!isNestedTransaction) tx.rollback
        throw ex
      }
      case ex => {
        if (!isNestedTransaction) tx.rollback()
        ex.printStackTrace()
        throw new code.exceptions.UnknownErrorException
      }
    } finally {
      if (!isNestedTransaction) em.close
    }
  }

  def deleteResource[T <: AnyRef](obj: T) = em.remove(obj)

}
//json: JValue, params: Map[String, List[String]],
//JValue, Map[String, List[String]]