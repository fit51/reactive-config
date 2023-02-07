resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

addSbtPlugin("org.scalameta"    % "sbt-scalafmt" % "2.4.6")
addSbtPlugin("com.typesafe.sbt" % "sbt-git"      % "1.0.0")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.2")
addSbtPlugin("org.lyranthe.fs2-grpc" % "sbt-java-gen" % "0.11.2")

addSbtPlugin("org.xerial.sbt"   % "sbt-sonatype" % "3.4")
addSbtPlugin("com.jsuereth"     % "sbt-pgp"      % "2.0.0")

libraryDependencies ++= Seq(
  "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-codegen" % "0.6.0-test8"
)
