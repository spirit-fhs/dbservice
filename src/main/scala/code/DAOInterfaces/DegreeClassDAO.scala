package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.DegreeClass

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.07.11
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */

trait DegreeClassDAO{
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): DegreeClass
  def read(req: Map[String, List[String]]): List[DegreeClass]
  def create(json: JValue): DegreeClass

  def readWithSubClasses(degreeClass: DegreeClass): List[DegreeClass]
}