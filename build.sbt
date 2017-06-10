name := "iF assignment"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, DockerPlugin)

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies += "org.projectlombok" % "lombok" % "1.16.16"
libraryDependencies += "com.google.code.morphia" % "morphia" % "0.104"
libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.4.2"
libraryDependencies += "com.palominolabs.http" % "url-builder" % "1.1.0"

PlayKeys.externalizeResources := false

testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v", "-Dconfig.file=conf/integration.conf"))

dockerExposedPorts := Seq(9000)