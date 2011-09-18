package code

import DAOInterfaces._
import eclipseLink.EclipseLinkDAOFactory

/**
 * 1. Provides some methods to implement by a DAOFactory
 * 2. Is a factory class to get a DAOFactory
 *
 * @version 1.0
 * @author Benjamin Lüdicke
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