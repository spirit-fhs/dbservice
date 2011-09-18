package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.DegreeClass

/**
 * This is the interface for degreeClass
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
trait DegreeClassDAO{
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): DegreeClass
  def read(req: Map[String, List[String]]): List[DegreeClass]
  def create(json: JValue): DegreeClass

  def readWithSubClasses(degreeClass: DegreeClass): List[DegreeClass]
}