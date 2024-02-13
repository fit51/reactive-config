val circeVersion = "0.14.1"
val logbackVersion = "1.4.5"

val scala212 = "2.12.17"
val scala213 = "2.13.11"
val allScalaVersions = List(scala212, scala213)

val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
val scalaTestContainers = "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.11" % Test
val catsEffectTestKit = "org.typelevel" %% "cats-effect-testkit" % "3.5.2" % Test

lazy val commonSettings = Seq(
  scalaVersion := scala213,
  crossScalaVersions := allScalaVersions,
  scalacOptions ++= List(
    "-feature",
    "-unchecked",
    "-deprecation",
    "-Ywarn-unused:imports",
    "-language:higherKinds",
    "-language:postfixOps"
  ) ::: (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 12)) => List("-Xexperimental")
    case Some((2, 13)) => List("-Ymacro-annotations")
    case _ => throw new Exception("!")
  }),
  libraryDependencies ++= Seq(
    "org.mockito"                % "mockito-core"   % "3.7.7" % Test,
    "org.scalatest"              %% "scalatest"     % "3.0.8" % Test
  ),
  scalafmtOnCompile := true,
  resolvers += Resolver.sonatypeRepo("releases"),
  addCompilerPlugin("org.typelevel" % "kind-projector"      % "0.13.2" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
  libraryDependencies ++= (
    if (CrossVersion.partialVersion(scalaVersion.value).contains((2, 12))) {
      compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full) :: Nil
    } else {
      Nil
    }
  )
)

lazy val `reactiveconfig-core` = project
  .in(file("core"))
  .settings(commonSettings)
  .settings(libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value)
  .settings(libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0")

lazy val `reactiveconfig-core-zio` = project
  .in(file("core-zio"))
  .settings(commonSettings)
  .settings(libraryDependencies += "dev.zio" %% "zio" % "2.0.21")
  .dependsOn(`reactiveconfig-core` % "compile->compile;test->test")

lazy val `reactiveconfig-core-ce` = project
  .in(file("core-ce"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(
    scalaLogging,
    "org.typelevel" %% "cats-effect" % "3.5.2",
    catsEffectTestKit
  ))
  .dependsOn(`reactiveconfig-core` % "compile->compile;test->test")

lazy val `reactiveconfig-circe` = project
  .in(file("circe"))
  .dependsOn(`reactiveconfig-core`)
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(
    scalaLogging,
    "io.circe" %% "circe-parser" % circeVersion % Provided,
    "io.circe" %% "circe-generic" % circeVersion % Test
  ))

lazy val `reactiveconfig-etcd` = project
  .in(file("etcd"))
  .dependsOn(`reactiveconfig-core`)
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(
    scalaLogging,
    "io.grpc" % "grpc-netty" % "1.61.0",
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
    "com.pauldijou" %% "jwt-core" % "4.3.0"
  ))
  .settings(
    Compile / PB.protoSources := Seq(
      (Compile / sourceDirectory).value / "protobuf_auth"
    ),
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
    )
  )

lazy val `reactiveconfig-etcd-ce` = project
  .in(file("etcd-ce"))
  .dependsOn(`reactiveconfig-core-ce`, `reactiveconfig-etcd`)
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(scalaTestContainers, catsEffectTestKit))
  .settings(
    Compile / PB.protoSources := Seq(
      (`reactiveconfig-etcd` / Compile / sourceDirectory).value / "protobuf"
    )
  )
  .enablePlugins(Fs2Grpc)

lazy val `reactiveconfig-etcd-zio` = project
  .in(file("etcd-zio"))
  .dependsOn(`reactiveconfig-core-zio`, `reactiveconfig-etcd`)
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(scalaTestContainers))
  .settings(
    Compile / PB.protoSources := Seq(
      (`reactiveconfig-etcd` / Compile / sourceDirectory).value / "protobuf"
    ),
    Compile / PB.targets := Seq(
      scalapb.gen(grpc = true) -> (Compile / sourceManaged).value,
      scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value / "scalapb"
    )
  )

lazy val `reactiveconfig-typesafe` = project
  .in(file("typesafe"))
  .settings(commonSettings)
  .settings(libraryDependencies += "com.typesafe" % "config" % "1.4.2")
  .dependsOn(`reactiveconfig-core`)

lazy val `reactiveconfig-typesafe-ce` = project
  .in(file("typesafe-ce"))
  .settings(commonSettings)
  .dependsOn(`reactiveconfig-core-ce`, `reactiveconfig-typesafe`)
  .settings(libraryDependencies ++= List(
    "co.fs2" %% "fs2-io" % "3.9.3",
    "io.circe" %% "circe-parser" % circeVersion % Test,
    catsEffectTestKit
  ))

lazy val `reactiveconfig-typesafe-zio` = project
  .in(file("typesafe-zio"))
  .settings(commonSettings)
  .dependsOn(`reactiveconfig-core-zio`, `reactiveconfig-typesafe`)
  .settings(libraryDependencies ++= List(
    "dev.zio" %% "zio-nio" % "2.0.0",
    "io.circe" %% "circe-parser" % circeVersion % Test
  ))

lazy val examples = project
  .in(file("examples"))
  .settings(commonSettings)
  .dependsOn(`reactiveconfig-etcd-ce`, `reactiveconfig-typesafe-ce`, `reactiveconfig-circe`)
  .settings(
    name := "reactiveconfig-examples",
    publish / skip := true,
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "ch.qos.logback" % "logback-core"    % logbackVersion,
      "io.circe"       %% "circe-generic"  % circeVersion,
      "io.circe"       %% "circe-parser"   % circeVersion
    )
  )
