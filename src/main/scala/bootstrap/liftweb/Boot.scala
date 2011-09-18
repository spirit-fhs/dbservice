package bootstrap.liftweb

import net.liftweb._
import common.{Logback, Full, Logger}
import http._

import auth.{userRoles, HttpBasicAuthentication, AuthRole}
import code.{MyDB, DAOFactory, ServiceApi}
import java.net.URL

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 *
 * 1. Responsible for http basic authentication
 */
class Boot {
  def boot {

    //roles for different permissions
    val roles =
      AuthRole("Admin",
        AuthRole("Standard")
      )

    LiftRules.httpAuthProtectedResource.append {
      //case Req(_ :: _, _, PutRequest) => roles.getRoleByName("Standard")
      //case Req(_ :: _, _, PostRequest) => roles.getRoleByName("Standard")
      //case Req(_ :: _, _, DeleteRequest) => roles.getRoleByName("Standard")
      case Req("protected" :: Nil, _, _) => roles.getRoleByName("Standard")
    }

    LiftRules.authentication = HttpBasicAuthentication("code") {
      case ("Standard", "Standard2011", req) =>
        userRoles(AuthRole("Standard"))
        true
      case ("Admin", "Admin2011", req) =>
        userRoles(AuthRole("Admin"))
        true
    }

    //select a appropriate database factory
    MyDB.factory = DAOFactory.getDAOFactory(DAOFactory.ECLIPSELINK)
    LiftRules.unloadHooks.append(MyDB.factory.closeConnection)

    LiftRules.statelessDispatchTable.append(ServiceApi)
  }
}
