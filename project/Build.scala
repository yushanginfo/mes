import org.beangle.parent.Dependencies._
import sbt.Keys._
import sbt._

object MesSettings {

  val common = Seq(
    organizationName := "The YushangInfo Software",
    licenses += ("GNU General Public License version 3", new URL("http://www.gnu.org/licenses/gpl-3.0.txt")),
    startYear := Some(2020),
    scalaVersion := "3.1.2",
    scalacOptions := Seq("-Xtarget:11", "-deprecation", "-feature"),
    javacOptions := Seq("--release", "11", "-encoding", "utf-8"),
    crossPaths := true,

    publishMavenStyle := true,
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishM2Configuration := publishM2Configuration.value.withOverwrite(true),
    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),

    versionScheme := Some("early-semver"),
    pomIncludeRepository := { _ => false }, // Remove all additional repository other than Maven Central from POM
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials"),
    resolvers += Resolver.mavenLocal
  )
}

object MesDepends {
  val commonsVer = "5.2.16"
  val dataVer = "5.4.4"
  val cdiVer = "0.3.6"
  val webVer = "0.0.6"
  val serializerVer = "0.0.23"
  val cacheVer = "0.0.26"
  val templateVer = "0.0.37"
  val webmvcVer = "0.5.0"
  val securityVer = "4.2.34"
  val idsVer = "0.2.27"
  val notifyVer = "0.0.5"
  val emsVer = "4.3.1"

  val commonsCore = "org.beangle.commons" %% "beangle-commons-core" % commonsVer
  val commonsFile = "org.beangle.commons" %% "beangle-commons-file" % commonsVer
  val dataJdbc = "org.beangle.data" %% "beangle-data-jdbc" % dataVer
  val dataOrm = "org.beangle.data" %% "beangle-data-orm" % dataVer
  val dataTransfer = "org.beangle.data" %% "beangle-data-transfer" % dataVer
  val cdiApi = "org.beangle.cdi" %% "beangle-cdi-api" % cdiVer
  val cdiSpring = "org.beangle.cdi" %% "beangle-cdi-spring" % cdiVer
  val cacheApi = "org.beangle.cache" %% "beangle-cache-api" % cacheVer
  val cacheCaffeine = "org.beangle.cache" %% "beangle-cache-caffeine" % cacheVer
  val notifyCore = "org.beangle.notify" %% "beangle-notify-core" % notifyVer
  val templateApi = "org.beangle.template" %% "beangle-template-api" % templateVer
  val templateFreemarker = "org.beangle.template" %% "beangle-template-freemarker" % templateVer
  val webAction = "org.beangle.web" %% "beangle-web-action" % webVer
  val webServlet = "org.beangle.web" %% "beangle-web-servlet" % webVer
  val webmvcCore = "org.beangle.webmvc" %% "beangle-webmvc-core" % webmvcVer
  val webmvcFreemarker = "org.beangle.webmvc" %% "beangle-webmvc-freemarker" % webmvcVer
  val webmvcSupport = "org.beangle.webmvc" %% "beangle-webmvc-support" % webmvcVer
  val webmvcSpring = "org.beangle.webmvc" %% "beangle-webmvc-spring" % webmvcVer
  val webmvcBootstrap = "org.beangle.webmvc" %% "beangle-webmvc-bootstrap" % webmvcVer
  val serializerText = "org.beangle.serializer" %% "beangle-serializer-text" % serializerVer
  val securityCore = "org.beangle.security" %% "beangle-security-core" % securityVer
  val securityWeb = "org.beangle.security" %% "beangle-security-web" % securityVer
  val securitySession = "org.beangle.security" %% "beangle-security-session" % securityVer
  val securityCas = "org.beangle.security" %% "beangle-security-cas" % securityVer
  val idsCas = "org.beangle.ids" %% "beangle-ids-cas" % idsVer
  val idsWeb = "org.beangle.ids" %% "beangle-ids-web" % idsVer
  val emsApp = "org.beangle.ems" %% "beangle-ems-app" % emsVer

  val apiDepends = Seq(scalatest, dataOrm, hibernate_core, notifyCore, cdiApi, templateFreemarker, logback_classic, scalatest, emsApp)

  val webDepends = Seq(scalatest, webAction, webmvcSupport, webmvcSpring, webmvcFreemarker, webmvcBootstrap,
    dataTransfer, hibernate_core, hibernate_jcache, ehcache, jaxb, jaxb_impl, HikariCP)

  val runtimeDepends = Seq(postgresql, logback_classic, serializerText)
}
