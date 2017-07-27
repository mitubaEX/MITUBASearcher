name := """MITUBASearcher"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

//resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + ".m2/repository"
resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.github.pochi" % "pochi-runner" % "1.0-SNAPSHOT",
  // https://mvnrepository.com/artifact/io.spray/spray-json_2.11
  "io.spray" % "spray-json_2.11" % "1.3.3",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.3"
)

