package code

import DAOInterfaces._
import eclipseLink.EclipseLinkDAOFactory

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.07.11
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */

object DAOFactory{
  val ECLIPSELINK = 1

  def getDAOFactory(whichDAOFactory: Int): DAOFactory = {
    whichDAOFactory match {
      case 1 => new EclipseLinkDAOFactory
      case _ => null
    }
  }

}

abstract class DAOFactory {

  def getConnection(): AnyRef
  def closeConnection(): Unit

  def getAppointmentDAO : AppointmentDAO
  def getDegreeClassDAO : DegreeClassDAO
  def getEventDAO : EventDAO
  def getLocationDAO : LocationDAO
  def getMemberDAO : MemberDAO
  def getMemberEventDAO : MemberEventDAO
  def getNewsCommentDAO : NewsCommentDAO
  def getNewsDAO : NewsDAO

}