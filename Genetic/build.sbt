name := "Lab3"

version := "1.0"

scalaVersion := "2.12.0-M3"

connectInput in run := true

assemblyJarName in assembly := "Lab3.jar"

mainClass in assembly := Some("genetic.main.UserMain")

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.2"

libraryDependencies += "it.unimi.dsi" % "dsiutils" % "2.3.0"