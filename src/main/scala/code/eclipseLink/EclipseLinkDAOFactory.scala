package code.eclipseLink

import code.DAOInterfaces._
import code.DAOFactory
import DAO._
import javax.persistence.{EntityManagerFactory, Persistence}

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 17.07.11
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */

class EclipseLinkDAOFactory extends DAOFactory{

  private val connection = Persistence.createEntityManagerFactory("TestUnit")

  def closeConnection() = {
    if(connection isOpen)
      connection close
  }

  def getConnection(): EntityManagerFactory = connection

  def getNewsDAO: NewsDAO = ELNewsDAO()
  def getNewsCommentDAO: NewsCommentDAO = ELNewsCommentDAO()
  def getMemberEventDAO: MemberEventDAO = ELMemberEventDAO()
  def getMemberDAO: MemberDAO = ELMemberDAO()
  def getLocationDAO: LocationDAO = ELLocationDAO()
  def getEventDAO: EventDAO = ELEventDAO()
  def getDegreeClassDAO: DegreeClassDAO = ELDegreeClassDAO()
  def getAppointmentDAO: AppointmentDAO = ELAppointmentDAO()
}