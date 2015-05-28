


import java.io._
import org.stormenroute.mecha._
import sbt._
import sbt.Keys._
import sbt.Process._



object ChronoDbBuild extends MechaRepoBuild {

  def repoName = "chrono-db"

  /* chrono-db */

  val frameworkVersion = Def.setting {
    ConfigParsers.versionFromFile(
      (baseDirectory in chronoDb).value / "version.conf",
      List("chrono_db_major", "chrono_db_minor"))
  }

  val chronoDbCrossScalaVersions = Def.setting {
    val dir = (baseDirectory in chronoDb).value
    val path = dir + File.separator + "cross.conf"
    scala.io.Source.fromFile(path).getLines.filter(_.trim != "").toSeq
  }

  val chronoDbScalaVersion = Def.setting {
    chronoDbCrossScalaVersions.value.head
  }

  val chronoDbSettings = Defaults.defaultSettings ++
    MechaRepoPlugin.defaultSettings ++ Seq(
    name := "chrono-db",
    organization := "com.storm-enroute",
    version <<= frameworkVersion,
    scalaVersion <<= chronoDbScalaVersion,
    crossScalaVersions <<= chronoDbCrossScalaVersions,
    libraryDependencies <++= (scalaVersion)(sv => dependencies(sv)),
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-optimise",
      "-Yinline-warnings"
    ),
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at
        "https://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype OSS Releases" at
        "https://oss.sonatype.org/content/repositories/releases"
    ),
    publishMavenStyle := true,
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra :=
      <url>http://storm-enroute.com/</url>
      <licenses>
        <license>
          <name>BSD-style</name>
          <url>http://opensource.org/licenses/BSD-3-Clause</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:storm-enroute/chrono-db.git</url>
        <connection>scm:git:git@github.com:storm-enroute/chrono-db.git</connection>
      </scm>
      <developers>
        <developer>
          <id>axel22</id>
          <name>Aleksandar Prokopec</name>
          <url>http://axel22.github.com/</url>
        </developer>
      </developers>,
    mechaPublishKey <<= mechaPublishKey.dependsOn(publish),
    mechaDocsRepoKey := "git@github.com:storm-enroute/apidocs.git",
    mechaDocsBranchKey := "gh-pages",
    mechaDocsPathKey := "chrono-db"
  )

  def dependencies(scalaVersion: String) =
    CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, major)) if major >= 11 => Seq(
      "org.scalatest" % "scalatest_2.11" % "2.1.7" % "test"
    )
    case Some((2, 10)) => Seq(
      "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test"
    )
    case _ => Nil
  }

  lazy val chronoDb: Project = Project(
    "chrono-db",
    file("."),
    settings = chronoDbSettings
  )

}
