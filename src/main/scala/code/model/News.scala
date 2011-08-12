package code.model

import java.util.Date
import net.liftweb.json.JsonDSL._
import scala.collection.JavaConverters._
import java.text.SimpleDateFormat
import net.liftweb.json.JsonAST.{JNull, JValue}
import code.Helper

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 09.05.11
 * Time: 15:32
 * To change this template use File | Settings | File Templates.
 */


class News extends Converter {

  var news_id: Long = _
  var creationDate: Date = _
  var expireDate: Date = _
  var lastModified: Date = _
  var title: String = _
  var content: String = _
  var owner: Member = _
  var comment: java.util.List[NewsComment] = _
  var degreeClass: java.util.List[DegreeClass] = _

  def getJNews_id: JValue = ("news_id" -> this.news_id: JValue)

  def getJCreationDate: JValue = ("creationDate" -> (if (this.creationDate == null) JNull: JValue else Helper.dateToIsoStringDate(this.creationDate): JValue))

  def getJExpireDate: JValue = ("expireDate" -> (if (this.expireDate == null) JNull: JValue else Helper.dateToIsoStringDate(this.expireDate): JValue))

  def getJLastModified: JValue = ("lastModified" -> (if (this.lastModified == null) JNull: JValue else Helper.dateToIsoStringDate(this.lastModified): JValue))

  def getJTitle: JValue = ("title" -> (if (this.title == null) JNull: JValue else this.title: JValue))

  def getJContent: JValue = ("content" -> (if (this.content == null) JNull: JValue else this.content: JValue))


  def objectToJValue: JValue = {

    val res =
      this.getJNews_id merge
        this.getJTitle merge
        this.getJContent merge
        ("owner" -> {
          if (this.owner == null)
            JNull: JValue
          else {
            owner.getJFhs_id merge
              owner.getJDisplayedName merge
              owner.getJMemberType
          }
        }) merge
        this.getJExpireDate merge
        this.getJCreationDate merge
        this.getJLastModified merge
        ("degreeClass" -> (this.degreeClass.asScala map (degreeClass =>
          degreeClass.getJClass_id merge
            degreeClass.getJTitle merge
            degreeClass.getJParent_id merge
            degreeClass.getJClassType merge
            degreeClass.getJMail
          ))) merge
        ("newsComment" -> (this.comment.asScala map (newsComment =>
          newsComment.getJComment_id merge
            newsComment.getJContent merge
            newsComment.getJCreationDate merge
            ("owner" -> {
              if (newsComment.owner == null)
                JNull: JValue
              else {
                newsComment.owner.getJFhs_id merge
                  newsComment.owner.getJDisplayedName merge
                  newsComment.owner.getJMemberType
              }
            })
          )))
    res
  }

  def prePersist() = {
    val date = Helper.dateToIsoDate(new Date)
    this.lastModified = date
    this.creationDate = date
  }

  def preUpdate() = {
    this.lastModified = Helper.dateToIsoDate(new Date)
  }

}

//@Entity
//class News {
//
//  @Id
//  var id: Long = _
//  @Temporal(TemporalType.TIMESTAMP)
//  var creationDate: Date = _
//  @Temporal(TemporalType.TIMESTAMP)
//  var expireDate: Date = _
//  @Temporal(TemporalType.TIMESTAMP)
//  var lastModified: Date = _
//  @Column(length = 50)
//  var title: String = _
//  @Column(length = 5000)
//  var content: String = _
//  @ManyToOne
//  @JoinColumn(nullable = false)
//  var owner: Member = _
//  @OneToMany(cascade = Array(CascadeType.ALL), mappedBy = "news")
//  var newsComment: java.util.List[NewsComment] = _
//  @JoinTable(
//    name = "News_Class",
//    joinColumns = Array(new JoinColumn(name = "news_id", referencedColumnName = "ID")),
//    inverseJoinColumns = Array(new JoinColumn(name = "degree_group_class_id", referencedColumnName = "MAIL"))
//  )
//  var degreeClass: java.util.List[DegreeClass_OLD] = _
//}