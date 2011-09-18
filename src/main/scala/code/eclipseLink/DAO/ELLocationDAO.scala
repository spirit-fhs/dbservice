package code.eclipseLink.DAO

import net.liftweb.json.JsonAST._
import code.model.{LocationPK, Location}
import code.MyDB
import javax.persistence.{EntityManagerFactory, EntityManager}

/**
 * This class represents a DAO for locations
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
object ELLocationDAO {
  val con = MyDB.factory.getConnection().asInstanceOf[EntityManagerFactory]

  def apply() = new ELLocationDAO(con.createEntityManager())

  def apply(em: EntityManager) = new ELLocationDAO(em)
}

class ELLocationDAO(em: EntityManager) extends AbstractPersistence[Location](em) with code.DAOInterfaces.LocationDAO {

  def delete(params: Map[String, List[String]]) = JNull

  def update(json: JValue, params: Map[String, List[String]]) = null

  def read(params: Map[String, List[String]]): List[Location] = {
    def function(): AnyRef = {
      var result: List[Location] = Nil
      (params.get("room"), params.get("building")) match {
        case (Some(room_ids), Some(building_ids)) if (room_ids.length == building_ids.length) => {
          result = (for (
            index <- 0 to building_ids.length - 1
          ) yield LocationPK(building_ids.slice(index, index + 1).head, room_ids.slice(index, index + 1).head)).toList.map(pk => {
            getResourceById(classOf[Location], pk)
          })
        }
        case (_, _) => {
          throw new code.exceptions.FindByCriteriaException
        }
      }
      result
    }
    invoke(function).asInstanceOf[List[Location]]
  }

  def create(json: JValue) = null
}