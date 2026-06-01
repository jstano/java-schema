package com.stano.buildlogic

val dependencyList = listOf(
  "ch.qos.logback:logback-classic:1.5.33",
  "ch.qos.logback:logback-core:1.5.33",
  "com.stano:java-utils:1.0.0",
  "com.stano:jdbc-utils:1.0.1",
  "commons-cli:commons-cli:1.11.0",
  "commons-io:commons-io:2.22.0",
  "net.bytebuddy:byte-buddy:1.18.8",
  "net.logstash.logback:logstash-logback-encoder:9.0",
  "org.apache.commons:commons-collections4:4.5.0",
  "org.apache.commons:commons-lang3:3.20.0",
  "org.apache.commons:commons-text:1.15.0",
  "org.apache.groovy:groovy-all:4.0.32",
  "org.flywaydb:flyway-core:12.7.0",
  "org.flywaydb:flyway-sqlserver:12.7.0",
  "org.junit.jupiter:junit-jupiter:6.1.0",
  "org.junit.platform:junit-platform-launcher:6.1.0",
  "org.liquibase:liquibase-core:5.0.3",
  "org.mockito:mockito-junit-jupiter:5.23.0",
  "org.postgresql:postgresql:42.7.11",
  "org.slf4j:jcl-over-slf4j:2.0.18",
  "org.slf4j:jul-to-slf4j:2.0.18",
  "org.slf4j:log4j-over-slf4j:2.0.18",
  "org.slf4j:slf4j-api:2.0.18",
  "org.spockframework:spock-core:2.4-groovy-4.0",
  "uk.org.lidalia:sysout-over-slf4j:1.0.2",
)

fun getFullDependency(dependency: String): String {
  return dependencyList.find { it.startsWith(dependency) } ?: throw IllegalArgumentException("Dependency $dependency not found")
}
