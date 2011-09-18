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

/**
 * This class contains methods to transform a timestamp
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
object Helper {

  private val  isoDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  private val  pathParamDate = new SimpleDateFormat("yyyyMMddHHmmss")

  /**
   * transform a date with timezone to a string with yyyy-MM-dd HH:mm:ss
   */
  def dateToIsoStringDate(date: Date): String = {
    isoDate.format(date)
  }
  /**
   * transform a date with timezone to a data with yyyy-MM-dd HH:mm:ss
   */
  def dateToIsoDate(date: Date) : Date = {
    isoStringDateToDate(isoDate.format(date))
  }
  /**
   * transform a string with yyyyMMddHHmmss to a date with yyyy-MM-dd HH:mm:ss
   */
  def pathParamToDate(date: String): Date = {
    pathParamDate.parse(date)
  }
  /**
   * transform a string with yyyy-MM-dd HH:mm:ss to a date with yyyy-MM-dd HH:mm:ss
   */
  def isoStringDateToDate(date: String): Date = {
    isoDate.parse(date)
  }

}