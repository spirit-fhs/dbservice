package code.DAOInterfaces

import net.liftweb.json.JsonAST.JValue
import code.model.NewsComment

/**
 * This is the interface for comments
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */trait NewsCommentDAO {
  def delete(req: Map[String, List[String]]): JValue
  def update(json: JValue, params: Map[String, List[String]]): NewsComment
  def read(req: Map[String, List[String]]): List[NewsComment]
  def create(json: JValue): NewsComment
}