addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.25")
addSbtPlugin("com.typesafe.sbt" % "sbt-git"      % "0.9.3")
addSbtPlugin("org.scalameta"    % "sbt-scalafmt" % "2.0.0")
addSbtPlugin("org.xerial.sbt"   % "sbt-sonatype" % "2.3")
addSbtPlugin("com.jsuereth"     % "sbt-pgp"      % "1.1.1")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.9.1"