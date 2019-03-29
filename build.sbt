lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  libraryDependencies ++= Seq(
    "io.monix"                   %% "monix"          % "3.0.0-RC2",
    "org.mockito"                % "mockito-core"    % "2.7.19" % Test,
    "org.scalatest"              %% "scalatest"      % "3.0.4" % Test,
    "org.slf4j"                  % "slf4j-api"       % "1.7.7",
    "com.typesafe.scala-logging" %% "scala-logging"  % "3.7.2",
    "ch.qos.logback"             % "logback-classic" % "1.2.3" % Test,
    "ch.qos.logback"             % "logback-core"    % "1.2.3" % Test
  ),
  scalafmtOnCompile := true
)

lazy val core = project
  .in(file("core"))
  .settings(commonSettings)
  .settings(
    name := "reactive-config-core",
    libraryDependencies ++= Seq(
      )
  )

lazy val circe = project
  .in(file("circe"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    name := "reactive-config-circe",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-magnolia-derivation" % "0.1.1",
      "io.circe" %% "circe-parser"              % "0.10.0-M1",
      "io.circe" %% "circe-generic"             % "0.9.3",
      "io.circe" %% "circe-literal"             % "0.9.3"
    )
  )

scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-unchecked",
  "-deprecation",
  "-Ywarn-unused:imports",
  "-Ypartial-unification",
  "-language:higherKinds"
)
