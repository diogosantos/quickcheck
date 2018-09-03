name := "quickcheck"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.16",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.16" % "test",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)