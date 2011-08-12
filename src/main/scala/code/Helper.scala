package code


import java.text.SimpleDateFormat
import java.util.Date
import model.DegreeClass


/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 15.06.11
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */

object Helper {

  private val  isoDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  private val  pathParamDate = new SimpleDateFormat("yyyyMMddHHmmss")

  def dateToIsoStringDate(date: Date): String = {
    isoDate.format(date)
  }

  def dateToIsoDate(date: Date) : Date = {
    isoStringDateToDate(isoDate.format(date))
  }

  def pathParamToDate(date: String): Date = {
    pathParamDate.parse(date)
  }

  def isoStringDateToDate(date: String): Date = {
    isoDate.parse(date)
  }

}