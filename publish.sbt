enablePlugins(GitVersioning)

git.useGitDescribe := true

ThisBuild / organization := "com.github.fit51"
ThisBuild / homepage := Some(url("https://github.com/fit51/reactive-config"))

ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/fit51/reactive-config"), "git@github.com:fit51/reactive-config.git")
)
ThisBuild / developers := List(
  Developer("fit51", "Pavel Kondratyuk", "fit511@yandex.ru", url("https://github.com/fit51")),
  Developer("danilbykov", "Danil Bykov", "d.bykov@tinkoff.ru", url("https://github.com/danilbykov")),
  Developer("realvikr", "Viktor Sinchikov", "v.sinchikov@tinkoff.ru", url("https://github.com/realvikr"))
)
ThisBuild / licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / publishMavenStyle := true

ThisBuild / publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

ThisBuild / pomIncludeRepository := { _ => false }

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

ThisBuild / updateOptions := updateOptions.value.withGigahorse(false)
