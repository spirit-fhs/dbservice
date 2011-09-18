package code.eclipseLink.DAO

import org.eclipse.persistence.expressions.Expression
import scala.collection.JavaConversions._
import org.eclipse.persistence.jpa.JpaHelper
import code.exceptions.{MappingException, FindByCriteriaException}
import net.liftweb.json.JsonAST._
import code.Helper
import org.eclipse.persistence.queries.ReadAllQuery
import javax.persistence.{EntityTransaction, Query, EntityManager}

/**
 * This class is provides standard functions to save or get data from the database
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
abstract class AbstractPersistence[T <: AnyRef : ClassManifest](em: EntityManager) {

  private[this] val readAll = new ReadAllQuery(classManifest[T].erasure)

  /**
   * Concatenate all Expressions with "and"
   *
   * @param list of expressions
   * @return returns a concatenated expression
   */
  def concatQuerysWithAnd(exp: List[Expression]): Expression = {
    val complex: Expression = null
    exp.foldLeft(complex) {
      case (r, c) if (r == null && c != null) => c
      case (r, c) if (r != null && c != null) => r.and(c)
      case (r, c) if (r == null || c == null) => r
    }
  }
  /**
   * Concatenate all Expressions with "or"
   *
   * @param list of expressions
   * @return returns a concatenated expression
   */
  def concatQuerysWithOr(exp: List[Expression]): Expression = {
    val complex: Expression = null
    exp.foldLeft(complex) {
      case (r, c) if (r == null && c != null) => c
      case (r, c) if (r != null && c != null) => r.or(c)
      case (r, c) if (r == null || c == null) => r
    }
  }

  /**
   * Get a database entity by given id
   */
  def getResourceById[T <: AnyRef](clazz: Class[T], pk: Any): T = {
    val res = em.find(clazz, pk)
    if (res == null)
      throw new FindByCriteriaException
    res
  }

  /**
   * Adds an order by to the query
   */
  def addOrderBy(orderExpression: Expression): Unit = {
    readAll.addOrdering(orderExpression)
  }

  /**
   * Get some entities by a given expression
   */
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

  /**
   * Map some data to the model and save it into the database
   */
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

  /**
   * Invoke a given function and do expression handling
   */
  def invoke (f:()=> AnyRef): AnyRef = {
    var tx: EntityTransaction = em.getTransaction
    try {
      if (!tx.isActive) tx.begin
      val newObj = f()
      if (!tx.isActive) tx.commit
      newObj
    } catch {
      case ex: code.exceptions.FormatedException => {
        if (!tx.isActive) tx.rollback
        throw ex
      }
      case ex => {
        if (!tx.isActive) tx.rollback()
        throw new code.exceptions.UnknownErrorException
      }
    } finally {
      if (!tx.isActive) em.close
    }
  }

  /**
   * Delete a entity
   */
  def deleteResource[T <: AnyRef](obj: T) = em.remove(obj)

}
