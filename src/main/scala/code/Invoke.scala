package code

import net.liftweb.json.JsonAST.JValue
import net.liftweb.common.Full
import net.liftweb.http.JsonResponse._
import net.liftweb.http.XmlResponse._
import net.liftweb.http._
import xml.Node
import net.liftweb.json.Xml

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 15.07.11
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */

abstract class Invoke {

  protected implicit def toXml(json: JValue): Node = <root>
    {Xml.toXml(json)}
  </root>

  protected implicit def toJson(xml: Node): JValue = Xml.toJson(xml)

  protected def responseBuilder(req: Req, code: Int, msg: JValue): LiftResponse = {
    req.accepts match {
      case Full("application/json") => JsonResponse(msg, Nil, Nil, code)
      case Full("application/xml") => XmlResponse(msg, code, "", Nil)
      case _ => new UnsupportedMediaTypeResponse
    }
  }

  def invoke(req: Req, params: Map[String, List[String]], successCode: Int,
                     f: (JValue, Map[String, List[String]]) => JValue): LiftResponse = {
    try {
      start(req, params, successCode, f)
    } catch {
      case ex: code.exceptions.FormatedException => responseBuilder(req, ex.getHttpStatusCode, ex.getErrorAsJson)
      case err => {
        println(err.printStackTrace)
        val ex = new code.exceptions.UnknownErrorException
        responseBuilder(req, ex.getHttpStatusCode, ex.getErrorAsJson)
      }
    }
  }

  protected def start(req: Req, params: Map[String, List[String]], successCode: Int,
                     f: (JValue, Map[String, List[String]]) => JValue): LiftResponse

}