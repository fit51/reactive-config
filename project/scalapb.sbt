resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.25")
addSbtPlugin("com.typesafe.sbt" % "sbt-git"      % "1.0.0")
addSbtPlugin("org.scalameta"    % "sbt-scalafmt" % "2.0.0")
addSbtPlugin("org.xerial.sbt"   % "sbt-sonatype" % "3.4")
addSbtPlugin("com.jsuereth"     % "sbt-pgp"      % "2.0.0")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.9.4"