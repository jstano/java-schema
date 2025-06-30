plugins {
  id("java-platform")
}

javaPlatform {
  allowDependencies()
}

dependencies {
  constraints {
    api("ch.qos.logback:logback-classic:1.5.18")
    api("ch.qos.logback:logback-core:1.5.18")
    api("com.stano:java-utils:1.0.0")
    api("com.stano:jdbc-utils:1.0.1")
    api("commons-cli:commons-cli:1.9.0")
    api("commons-io:commons-io:2.19.0")
    api("net.bytebuddy:byte-buddy:1.17.6")
    api("net.logstash.logback:logstash-logback-encoder:8.1")
    api("org.apache.commons:commons-collections4:4.5.0")
    api("org.apache.commons:commons-lang3:3.17.0")
    api("org.apache.groovy:groovy-all:4.0.27")
    api("org.junit.jupiter:junit-jupiter:5.13.2")
    api("org.junit.platform:junit-platform-launcher:1.13.2")
    api("org.liquibase:liquibase-core:4.32.0")
    api("org.mockito:mockito-junit-jupiter:5.18.0")
    api("org.postgresql:postgresql:42.7.7")
    api("org.slf4j:jcl-over-slf4j:2.0.17")
    api("org.slf4j:jul-to-slf4j:2.0.17")
    api("org.slf4j:log4j-over-slf4j:2.0.17")
    api("org.slf4j:slf4j-api:2.0.17")
    api("org.spockframework:spock-core:2.3-groovy-4.0")
    api("uk.org.lidalia:sysout-over-slf4j:1.0.2")
  }
}
