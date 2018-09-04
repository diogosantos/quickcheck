name := "quickcheck"

version := "0.1"

scalaVersion := "2.11.12"

val akkaV = "2.5.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)