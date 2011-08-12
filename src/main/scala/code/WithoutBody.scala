package code

import net.liftweb.json.JsonAST.JValue
import net.liftweb.http.{LiftResponse, Req}

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 15.07.11
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */

class WithoutBody extends Invoke{

  override def start(req: Req, params: Map[String, List[String]], successCode: Int,
                     f: (JValue, Map[String, List[String]]) => JValue): LiftResponse = {
    responseBuilder(req, successCode, f(null, params))
  }

}