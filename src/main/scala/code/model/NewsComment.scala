package code.model

import java.util.Date
import net.liftweb.json.JsonDSL._
import java.text.SimpleDateFormat
import net.liftweb.json.JsonAST.{JNull, JValue}
import code.Helper

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 09.05.11
 * Time: 22:15
 * To change this template use File | Settings | File Templates.
 */

class NewsComment extends Converter {

  var comment_id: Long = _
  var content: String = _
  var creationDate: Date = _
  var owner: Member = _
  var news: News = _

  def getJComment_id: JValue = ("comment_id" -> this.comment_id: JValue)

  def getJContent: JValue = ("content" -> (if (this.content == null) JNull: JValue else this.content: JValue))

  def getJCreationDate: JValue = ("creationDate" -> (if (this.creationDate == null) JNull: JValue else Helper.dateToIsoStringDate(this.creationDate): JValue))


  def objectToJValue: JValue = {
    val res =
      this.getJComment_id merge
        ("owner" -> {
          if (this.owner == null)
            JNull: JValue
          else {
            this.owner.getJFhs_id merge
              this.owner.getJDisplayedName merge
              this.owner.getJMemberType
          }
        }) merge
        this.getJCreationDate merge
        this.getJContent merge
        ("news" -> {
          if (this.news == null)
            JNull: JValue
          else
            news.getJNews_id
        })
    res
  }


  def prePersist() = {
    this.creationDate = Helper.dateToIsoDate(new Date)
  }

  //  def objectToJValue: JValue = {
  //    val l1 =
  //      ("comment_id" -> this.comment_id) ~
  //        ("owner" -> (
  //          ("fhs_id" -> this.owner.fhs_id) ~
  //            ("displayedName" -> this.owner.displayedName)
  //          )) ~
  //        ("creationDate" -> sdf.format(this.creationDate)) ~
  //        ("content" -> this.content) ~
  //        ("news_id" -> this.news.news_id)
  //    l1
  //  }
}


//@Entity
//class NewsComment {
//
//  @Id
//  var id: Long = _
//  @Column(length = 5000)
//  var content: String = _
//  @Temporal(TemporalType.TIMESTAMP)
//  var creationDate: Date = _
//  @ManyToOne
//  @JoinColumn(nullable = false)
//  var owner: Member = _
//  @ManyToOne
//  @JoinColumn(nullable = false)
//  var news: News = _
//
//}