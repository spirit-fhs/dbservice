package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.NewsComment
import code.MyDB

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.07.11
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */

trait NewsCommentDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): NewsComment
  def read(req: Map[String, List[String]]): List[NewsComment]
  def create(json: JValue): NewsComment
}