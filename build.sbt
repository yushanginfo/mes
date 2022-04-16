import MesDepends._
import MesSettings._

ThisBuild / organization := "com.yushanginfo.erp.mes"
ThisBuild / version := "0.0.3-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/yushanginfo/mes"),
    "scm:git@github.com:yushanginfo/mes.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "chaostone",
    name  = "Tihua Duan",
    email = "duantihua@gmail.com",
    url   = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "The YushangInfo MES Library"
ThisBuild / homepage := Some(url("http://yushanginfo.github.io/ems/index.html"))
ThisBuild / resolvers += Resolver.mavenLocal

lazy val root = (project in file("."))
  .settings()
  .aggregate(api,base,web,wo)

lazy val api = (project in file("api"))
  .settings(
    name := "erp-mes-api",
    common,
    crossPaths := false,
    libraryDependencies ++= apiDepends
  )

lazy val web = (project in file("web"))
  .settings(
    name := "erp-mes-web",
    common,
    crossPaths := false,
    libraryDependencies ++= webDepends
  ).dependsOn(api)

lazy val base = (project in file("base"))
  .enablePlugins(WarPlugin,UndertowPlugin)
  .settings(
    name := "erp-mes-base",
    common,
    crossPaths := false,
    libraryDependencies ++= runtimeDepends
  ).dependsOn(web)


lazy val wo = (project in file("wo"))
  .enablePlugins(WarPlugin,UndertowPlugin)
  .settings(
    name := "erp-mes-web",
    common,
    crossPaths := false,
    libraryDependencies ++= runtimeDepends
  ).dependsOn(web)

publish / skip := true
