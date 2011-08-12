package code.eclipseLink.DAO

import org.eclipse.persistence.expressions.{Expression, ExpressionBuilder}
import net.liftweb.json.JsonAST._
import scala.collection.JavaConverters._
import code.MyDB
import javax.persistence.{EntityManagerFactory, EntityManager}
import code.model.DegreeClass

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 12.06.11
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */

object ELDegreeClassDAO {
  val con = MyDB.factory.getConnection().asInstanceOf[EntityManagerFactory]

  def apply() = new ELDegreeClassDAO(con.createEntityManager())

  def apply(em: EntityManager) = new ELDegreeClassDAO(em, true)
}

class ELDegreeClassDAO(em: EntityManager, isNestedTransaction: Boolean = false) extends AbstractPersistence[DegreeClass](em, isNestedTransaction) with code.DAOInterfaces.DegreeClassDAO {

  def read(params: Map[String, List[String]]): List[DegreeClass] = {
    def function(): AnyRef = {
      val eb = new ExpressionBuilder
      var expressions: List[Expression] = Nil
      params.keys foreach {
        key => {
          key match {
            case "class_id" => return params.get(key).get map (class_id => getResourceById(classOf[DegreeClass], class_id.toLong))
            case "classType" => expressions ::= eb.get(key).equal(params.get(key).get.head)
            case "title" => expressions ::= eb.get(key).equal(params.get(key).get.head)
          }
        }
      }
      if (params.isEmpty) {
        return List(getResourceById(classOf[DegreeClass], 1L))
      } else {
        return getResourceByExpression(expressions)
      }
    }
    invoke(function).asInstanceOf[List[DegreeClass]]
  }

  def delete(params: Map[String, List[String]]): JValue = JNull

  def update(json: JValue, params: Map[String, List[String]]): DegreeClass = null

  def create(json: JValue): DegreeClass = null

  def readWithSubClasses(degreeClass: DegreeClass): List[DegreeClass] = {
    def function(): AnyRef = {
      def recursiv(clazzes: List[DegreeClass], acc: List[DegreeClass] = Nil): List[DegreeClass] = {
        if (clazzes.isEmpty) {
          return acc
        }
        else {
          val childs = clazzes.flatMap(node => node.subClasses.asScala.toList)
          recursiv(childs, clazzes ++ acc)
        }
      }
      recursiv(List(degreeClass))
    }
    invoke(function).asInstanceOf[List[DegreeClass]]
  }

}