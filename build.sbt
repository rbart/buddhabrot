name := "Buddhabrot rendering tools for Scala"

version := "1.0.0"

scalaVersion := "2.10.2"

libraryDependencies ++= {
  Seq(
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
    "com.github.scopt" %% "scopt" % "3.0.0"
  )
}

resolvers += "sonatype-public" at "https://oss.sonatype.org/content/groups/public"