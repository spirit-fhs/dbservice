package code

import net.liftweb.util.Helpers._
import net.liftweb.json.JsonDSL._
import net.liftweb.http.rest.RestHelper
import net.liftweb.http._
import net.liftweb.json._

/**
 * Provides an RESTFul API to invoke database operations
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
object ServiceApi extends RestHelper {

  println("Konstruktor der Service API")
  val connector: DBConnector = new DBConnector(MyDB.factory)
  val withoutBody: Invoke = new WithoutBody
  val withBody: Invoke = new WithBody
  var lastModifiedNews: java.util.Date = new java.util.Date

  serve {

    case "protected" :: Nil Get req => {
      JsonResponse(("message" -> "yeah!"), Nil, Nil, 200)
    }


    case "news" :: Nil Put req => {
      withBody.invoke(req, req.params, 201, connector.putNews)
    }

    case "news" :: "comment" :: Nil Put req => {
      lastModifiedNews = new java.util.Date
      withBody.invoke(req, req.params, 201, connector.putNewsComment)
    }

    case "news" :: Nil Get req => {
      withoutBody.invoke(req, req.params, 200, connector.getNews)
    }

    case "news" :: AsLong(news_id) :: Nil Get req => {
      var params = req.params
      params += "news_id" -> List(news_id.toString)
      withoutBody.invoke(req, params, 200, connector.getNews)
    }

    case "news" :: AsLong(news_id) :: "comment" :: Nil Get req => {
      var params = req.params
      params += ("news_id" -> List(news_id.toString))
      withoutBody.invoke(req, params, 200, connector.getNewsComment)
    }

    case "news" :: "comment" :: Nil Get req => {
      withoutBody.invoke(req, req.params, 200, connector.getNewsComment)
    }

    case "news" :: "comment" :: AsLong(comment_id) :: Nil Get req => {
      var params = req.params
      params += "comment_id" -> List(comment_id.toString)
      withoutBody.invoke(req, params, 200, connector.getNewsComment)
    }

    case "event" :: AsLong(event_id) :: Nil Get req => {
      var params = req.params
      params += "event_id" -> List(event_id.toString)
      withoutBody.invoke(req, params, 200, connector.getEvent)
    }

    case "event" :: Nil Get req => {
      withoutBody.invoke(req, req.params, 200, connector.getEvent)
    }

    case "event" :: Nil Put req => {
      withBody.invoke(req, req.params, 201, connector.putEvent)
    }

    case "appointment" :: Nil Get req => {
      withoutBody.invoke(req, req.params, 200, connector.getAppointment)
    }

    case "appointment" :: Nil Put req => {
      withBody.invoke(req, req.params, 201, connector.putAppointment)
    }

    case "appointment" :: AsLong(appointment_id) :: Nil Get req => {
      var params = req.params
      params += "appointment_id" -> List(appointment_id.toString)
      withoutBody.invoke(req, params, 200, connector.getAppointment)
    }

    case "degreeClass" :: Nil Get req => {
      withoutBody.invoke(req, req.params, 200, connector.getDegreeClass)
    }

    case "degreeClass" :: AsLong(class_id) :: Nil Get req => {
      var params = req.params
      params += "class_id" -> List(class_id.toString)
      withoutBody.invoke(req, params, 200, connector.getDegreeClass)
    }

    case "member" :: Nil Get req => {
      withoutBody.invoke(req, req.params, 200, connector.getMember)
    }

    case "member" :: Nil Put req => {
      withBody.invoke(req, req.params, 201, connector.putMember)
    }

    case "member" :: (fhs_id) :: Nil Get req => {
      var params = req.params
      params += "fhs_id" -> List(fhs_id.toString)
      withoutBody.invoke(req, params, 200, connector.getMember)
    }

    case "lastModified" :: "news" :: Nil Get req => {
      val lmn: JValue = ("lastModiefied" -> Helper.dateToIsoStringDate(lastModifiedNews))
      //responseBuilder(req, 200, lmn)
      // TODO Am besten letzte News zurückschicken?!
      null
    }

    case "location" :: (building) :: (room) :: Nil Get req => {
      var params = req.params
      params += "building" -> List(building.toString)
      params += "room" -> List(room.toString)
      withoutBody.invoke(req, req.params, 200, connector.getLocation)
    }


  }

  //Delete
  serve {
    case "news" :: "comment" :: AsLong(comment_id) :: Nil Delete req => {
      var params = req.params
      params += "comment_id" -> List(comment_id.toString)
      withoutBody.invoke(req, params, 200, connector.deleteNewsComment)
    }

    case "news" :: AsLong(news_id) :: Nil Delete req => {
      var params = req.params
      params += "news_id" -> List(news_id.toString)
      withoutBody.invoke(req, params, 200, connector.deleteNews)
    }

    case "event" :: AsLong(event_id) :: Nil Delete req => {
      var params = req.params
      params += "event_id" -> List(event_id.toString)
      withoutBody.invoke(req, params, 200, connector.deleteEvent)
    }

    case "member" :: (fhs_id) :: Nil Delete req => {
      var params = req.params
      params += "fhs_id" -> List(fhs_id)
      withoutBody.invoke(req, params, 200, connector.deleteMember)
    }

  }

  //Post
  serve {
    case "news" :: AsLong(news_id) :: Nil Post req => {
      var params = req.params
      params += "news_id" -> List(news_id.toString)
      withBody.invoke(req, params, 201, connector.postNews)
    }

    case "news" :: "comment" :: AsLong(comment_id) :: Nil Post req => {
      var params = req.params
      params += "comment_id" -> List(comment_id.toString)
      withBody.invoke(req, params, 201, connector.postNewsComment)
    }

    case "member" :: (fhs_id) :: Nil Post req => {
      var params = req.params
      params += "fhs_id" -> List(fhs_id)
      withBody.invoke(req, params, 201, connector.postMember)
    }

    case "appointment" :: (appointment_id) :: Nil Post req => {
      var params = req.params
      params += "appointment_id" -> List(appointment_id)
      withBody.invoke(req, params, 201, connector.postAppointment)
    }

    case _ => NotFoundResponse("Bad URI4")
  }

}
