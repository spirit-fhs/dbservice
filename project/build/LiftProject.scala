import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val liftVersion = "2.3"

  // uncomment the following if you want to use the snapshot repo
  val scalatoolsSnapshot = "EclipseLink" at "http://download.eclipse.org/rt/eclipselink/maven.repo"

  // If you're using JRebel for Lift development, uncomment
  // this line
  // override def scanDirectories = Nil

  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test",
    "junit" % "junit" % "4.5" % "test",
    "org.scalatest" % "scalatest_2.8.1" % "1.5.1" % "test",
    "ch.qos.logback" % "logback-classic" % "0.9.29",
    "org.scala-tools.testing" %% "specs" % "1.6.6" % "test",
    "postgresql" % "postgresql" % "9.0-801.jdbc4",
    "org.eclipse.persistence" % "javax.persistence" % "2.0.2",
    "org.eclipse.persistence" % "eclipselink" % "2.3.0"
  ) ++ super.libraryDependencies
}
