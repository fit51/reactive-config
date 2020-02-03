lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  crossScalaVersions := Seq("2.12.8", "2.13.1"),
  libraryDependencies ++= Seq(
    "io.monix"                   %% "monix"          % "3.0.0",
    "org.typelevel"              %% "cats-effect"    % "2.0.0",
    "com.typesafe.scala-logging" %% "scala-logging"  % "3.9.2",
    "org.mockito"                % "mockito-core"    % "2.7.19" % Test,
    "org.scalatest"              %% "scalatest"      % "3.0.8" % Test,
    "ch.qos.logback"             % "logback-classic" % "1.2.3" % Test,
    "ch.qos.logback"             % "logback-core"    % "1.2.3" % Test,
    "io.circe"                   %% "circe-generic"  % "0.12.2" % Test,
    "org.slf4j"                  % "slf4j-api"       % "1.7.28" % Test
  ),
  scalafmtOnCompile := true,
  resolvers += Resolver.sonatypeRepo("releases"),
  addCompilerPlugin("org.typelevel" % "kind-projector"      % "0.11.0" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
)

lazy val core = project
  .in(file("core"))
  .settings(commonSettings)
  .settings(
    name := "reactive-config-core"
  )

lazy val circe = project
  .in(file("circe"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    name := "reactive-config-circe",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-parser" % "0.12.2"
    )
  )

lazy val etcd = project
  .in(file("etcd"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    name := "reactive-config-etcd",
    libraryDependencies ++= Seq(
      "io.grpc"              % "grpc-netty"                      % "1.22.3",
      "io.netty"             % "netty-tcnative-boringssl-static" % "2.0.25.Final",
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc"           % scalapb.compiler.Version.scalapbVersion,
      "com.coreos"           % "jetcd-core"                      % "0.0.2" excludeAll (ExclusionRule(organization = "io.grpc")),
      "com.coreos"           % "jetcd-common"                    % "0.0.2" excludeAll (ExclusionRule(organization = "io.grpc"))
    )
  )
  .settings {
    val generateSources           = TaskKey.apply[Unit]("generateSources")
    def genPackage(f: File): File = f / "com" / "github" / "fit51" / "reactiveconfig" / "etcd" / "gen"
    generateSources := {
      (PB.generate in Compile).value
      val genDir    = genPackage((sourceManaged in Compile).value)
      val targetDir = genPackage((sourceDirectory in Compile).value / "scala")
      println(s"Generated in: $genDir")
      println(s"Moved to: $targetDir")
      IO.copyDirectory(genDir, targetDir, true, true)
      IO.delete(genDir)
    }
  }

lazy val typesafe = project
  .in(file("typesafe"))
  .dependsOn(core)
  .dependsOn(circe % "test->compile")
  .settings(commonSettings)
  .settings(
    name := "reactive-config-typesafe",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1",
      //    TODO monix-nio is more convenient for our purposes but it uses Monix v 3.0.0-M3 which is incompatible with 3.0.0-RC2
      //    TODO better-files used instead. One should use monix-nio in future
      "com.github.pathikrit" %% "better-files" % "3.8.0"
    )
  )

lazy val examples = project
  .in(file("examples"))
  .dependsOn(etcd, typesafe, circe)
  .settings(commonSettings)
  .settings(
    name := "reactive-config-examples",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "ch.qos.logback" % "logback-core"    % "1.2.3",
      "io.circe"       %% "circe-generic"  % "0.12.2"
    )
  )

scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-unchecked",
  "-deprecation",
  "-Ywarn-unused:imports",
  "-language:higherKinds",
  "-language:postfixOps"
)
