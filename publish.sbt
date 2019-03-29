organization in ThisBuild := "com.github.fit51"

val publishVersion = "0.0.1"

version in ThisBuild := {
  val branch = git.gitCurrentBranch.value
  if (branch == "master") publishVersion
  else s"${publishVersion}-$branch-SNAPSHOT"
}