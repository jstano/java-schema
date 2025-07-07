package com.stano.buildlogic

val dependencyList = listOf(
  "ch.qos.logback:logback-classic:1.5.18",
  "ch.qos.logback:logback-core:1.5.18",
  "com.stano:java-utils:1.0.0",
  "com.stano:jdbc-utils:1.0.1",
  "commons-cli:commons-cli:1.9.0",
  "commons-io:commons-io:2.19.0",
  "net.bytebuddy:byte-buddy:1.17.6",
  "net.logstash.logback:logstash-logback-encoder:8.1",
  "org.apache.commons:commons-collections4:4.5.0",
  "org.apache.commons:commons-lang3:3.17.0",
  "org.apache.commons:commons-text:1.13.1",
  "org.apache.groovy:groovy-all:4.0.27",
  "org.junit.jupiter:junit-jupiter:5.13.2",
  "org.junit.platform:junit-platform-launcher:1.13.2",
  "org.liquibase:liquibase-core:4.32.0",
  "org.mockito:mockito-junit-jupiter:5.18.0",
  "org.postgresql:postgresql:42.7.7",
  "org.slf4j:jcl-over-slf4j:2.0.17",
  "org.slf4j:jul-to-slf4j:2.0.17",
  "org.slf4j:log4j-over-slf4j:2.0.17",
  "org.slf4j:slf4j-api:2.0.17",
  "org.spockframework:spock-core:2.3-groovy-4.0",
  "uk.org.lidalia:sysout-over-slf4j:1.0.2",
)

fun getFullDependency(dependency: String): String {
  return dependencyList.find { it.startsWith(dependency) } ?: throw IllegalArgumentException("Dependency $dependency not found")
}
