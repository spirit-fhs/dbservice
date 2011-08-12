package code

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 11.05.11
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */

trait ConnectionFactory[T] {
  def getConnection() : T
  def closeConnection()
}