val circeVersion = "0.14.1"
val logbackVersion = "1.4.5"

val scala212 = "2.12.14"
val scala213 = "2.13.8"
val allScalaVersions = List(scala212, scala213)

val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
val scalaTestContainers = "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.11" % Test

lazy val commonSettings = Seq(
  scalaVersion := scala213,
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
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
)

lazy val `reactiveconfig-core` = projectMatrix
  .in(file("core"))
  .settings(commonSettings)
  .settings(libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value)
  .settings(libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0")
  .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `reactiveconfig-core-zio` = projectMatrix
  .in(file("core-zio"))
  .settings(commonSettings)
  .settings(libraryDependencies += "dev.zio" %% "zio" % "2.0.6")
  .dependsOn(`reactiveconfig-core` % "compile->compile;test->test")
  .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `reactiveconfig-core-ce` = projectMatrix
  .in(file("core-ce"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(scalaLogging, "org.typelevel" %% "cats-effect" % "2.5.4"))
  .dependsOn(`reactiveconfig-core` % "compile->compile;test->test")
  .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `reactiveconfig-circe` = projectMatrix
  .in(file("circe"))
  .dependsOn(`reactiveconfig-core`)
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(
    scalaLogging,
    "io.circe" %% "circe-parser" % circeVersion % Provided,
    "io.circe" %% "circe-generic" % circeVersion % Test
  ))
  .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `reactiveconfig-etcd` = projectMatrix
  .in(file("etcd"))
  .dependsOn(`reactiveconfig-core`)
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(
    scalaLogging,
    "io.grpc" % "grpc-netty" % "1.43.2",
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
  .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `reactiveconfig-etcd-ce` = projectMatrix
  .in(file("etcd-ce"))
  .dependsOn(`reactiveconfig-core-ce`, `reactiveconfig-etcd`)
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(scalaTestContainers))
  .settings(
    Compile / PB.protoSources := Seq(
      (`reactiveconfig-etcd`.jvm(scala213) / Compile / sourceDirectory).value / "protobuf"
    )
  )
  .enablePlugins(Fs2Grpc)
  .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `reactiveconfig-etcd-zio` = projectMatrix
  .in(file("etcd-zio"))
  .dependsOn(`reactiveconfig-core-zio`, `reactiveconfig-etcd`)
  .settings(commonSettings)
  .settings(libraryDependencies ++= List(scalaTestContainers))
  .settings(
    Compile / PB.protoSources := Seq(
      (`reactiveconfig-etcd`.jvm(scala213) / Compile / sourceDirectory).value / "protobuf"
    ),
    Compile / PB.targets := Seq(
      scalapb.gen(grpc = true) -> (Compile / sourceManaged).value,
      scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value / "scalapb"
    )
  )
  .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `reactiveconfig-typesafe` = projectMatrix
  .in(file("typesafe"))
  .settings(commonSettings)
  .settings(libraryDependencies += "com.typesafe" % "config" % "1.4.2")
  .dependsOn(`reactiveconfig-core`)
  .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `reactiveconfig-typesafe-ce` = projectMatrix
  .in(file("typesafe-ce"))
  .settings(commonSettings)
  .dependsOn(`reactiveconfig-core-ce`, `reactiveconfig-typesafe`)
  .settings(libraryDependencies ++= List(
    "co.fs2" %% "fs2-io" % "2.5.9",
    "io.circe" %% "circe-parser" % circeVersion % Test
  ))
 .jvmPlatform(scalaVersions = allScalaVersions)

lazy val `reactiveconfig-typesafe-zio` = projectMatrix
  .in(file("typesafe-zio"))
  .settings(commonSettings)
  .dependsOn(`reactiveconfig-core-zio`, `reactiveconfig-typesafe`)
  .settings(libraryDependencies ++= List(
    "dev.zio" %% "zio-nio" % "2.0.0",
    "io.circe" %% "circe-parser" % circeVersion % Test
  ))
  .jvmPlatform(scalaVersions = allScalaVersions)

lazy val examples = projectMatrix
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
      "io.circe"       %% "circe-parser"  % circeVersion
    )
  )
  .jvmPlatform(scalaVersions = allScalaVersions)
