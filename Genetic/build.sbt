name := "Lab1"

version := "1.0"

scalaVersion := "2.12.0-M3"

connectInput in run := true

assemblyJarName in assembly := "Lab1.jar"

mainClass in assembly := Some("main.UserMain")

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.2"