package code

import net.liftweb.json.JsonAST.JValue
import net.liftweb.common.Full
import net.liftweb.http.JsonResponse._
import net.liftweb.http.XmlResponse._
import net.liftweb.http._
import xml.Node
import net.liftweb.json.Xml


/**
 * This class create a XML or JSON response
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
abstract class Invoke {

  /**
   * Transform JSON to XML
   */
  protected implicit def toXml(json: JValue): Node = <root>
    {Xml.toXml(json)}
  </root>

  /**
   * Transform XML to JSON
   */
  protected implicit def toJson(xml: Node): JValue = Xml.toJson(xml)

  /**
   * Create a http-response with JSON or XML body
   */
  protected def responseBuilder(req: Req, code: Int, msg: JValue): LiftResponse = {
    req.accepts match {
      case Full("application/json") => JsonResponse(msg, Nil, Nil, code)
      case Full("application/xml") => XmlResponse(msg, code, "", Nil)
      case _ => new UnsupportedMediaTypeResponse
    }
  }

  /**
   * Invoke the given function
   *
   * @param req is the http request
   * @param params are the query params of the http-request
   * @param successCode sends back this http code if the invocation was successfully
   * @param f is a given function
   */
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

  /**
   * This function needs to overwrite and is encapsulate in function invoke
   *
   * @param req is the http request
   * @param params are the query params of the http-request
   * @param successCode sends back this http code if the invocation was successfully
   * @param f is a given function
   */
  protected def start(req: Req, params: Map[String, List[String]], successCode: Int,
                     f: (JValue, Map[String, List[String]]) => JValue): LiftResponse

}