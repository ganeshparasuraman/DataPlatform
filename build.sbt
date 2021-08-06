name := "DataPlatform"
scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  // Apache Spark core
  "org.apache.spark" %% "spark-sql" % "3.0.1" % "provided"

  // JSON parsing library
  , "org.json4s" %% "json4s-jackson" % "3.6.10"

)

//add dependencies marked as provided to classpath when run in local
run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in(Compile, run), runner in(Compile, run)).evaluated
runMain in Compile := Defaults.runMainTask(fullClasspath in Compile, runner in(Compile, run)).evaluated

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.last
  case PathList("META-INF","org","apache","logging","log4j","core","config","plugins", "Log4j2Plugins.dat") => MergeStrategy.last
  case PathList("com", "google", "gson", xs@_*) => MergeStrategy.last
  case PathList("org", "apache", "commons", "codec", xs@_*) => MergeStrategy.last
  case PathList("module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.common.**" -> "shadeio.@1").inAll,
  ShadeRule.rename("org.json4s.**" -> "shadejson4s.@1").inAll
)
