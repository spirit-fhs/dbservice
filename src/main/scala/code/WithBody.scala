package code

import net.liftweb.json._
import net.liftweb.common.{Failure, Full, Box}
import net.liftweb.http.{UnsupportedMediaTypeResponse, Req, LiftResponse}

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 15.07.11
 * Time: 20:22
 * To change this template use File | Settings | File Templates.
 */

/**
 * This Class is used when the http request has a body
 *
 * @version
 * @author Benjamin Lüdicke
 */
class WithBody extends Invoke {

  /**
   * Convert XML body to JSON
   */
  private def bodyToJson(req: Req): Box[JValue] = {
    req.contentType match {
      case Full("application/json") => {
        req.json
      }
      case Full("application/xml") => Full(req.xml.get)
      case _ => Failure("unsupported media type")
    }
  }

  /**
   * Execute the bodyToJson Method
   */
  override def start(req: Req, params: Map[String, List[String]], successCode: Int,
                     f: (JValue, Map[String, List[String]]) => JValue): LiftResponse = {

    bodyToJson(req) match {
      case Full(json: JValue) => {
        responseBuilder(req, successCode, f(json, params))
      }
      case _ => new UnsupportedMediaTypeResponse
    }
  }

}