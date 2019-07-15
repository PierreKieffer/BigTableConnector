name := "BigTableConnector"

version := "0.1"

scalaVersion := "2.11.8"

//// https://mvnrepository.com/artifact/com.google.cloud.bigtable/bigtable-hbase-1.x-hadoop
// https://mvnrepository.com/artifact/org.apache.hbase/hbase
//libraryDependencies += "org.apache.hbase" % "hbase" % "2.1.0"
//// https://mvnrepository.com/artifact/org.apache.hbase/hbase-client
//libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.1.0"
//// https://mvnrepository.com/artifact/org.apache.hbase/hbase-common
//libraryDependencies += "org.apache.hbase" % "hbase-common" % "2.1.0"
//// https://mvnrepository.com/artifact/org.apache.hbase/hbase-server
//libraryDependencies += "org.apache.hbase" % "hbase-server" % "2.1.0"
//// https://mvnrepository.com/artifact/org.apache.hbase/hbase-mapreduce
//libraryDependencies += "org.apache.hbase" % "hbase-mapreduce" % "2.1.0"
// https://mvnrepository.com/artifact/com.google.cloud.bigtable/bigtable-hbase-1.x
libraryDependencies += "com.google.cloud.bigtable" % "bigtable-hbase-1.x" % "1.11.0"



assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
  case "log4j.properties" => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") =>
    MergeStrategy.filterDistinctLines
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}