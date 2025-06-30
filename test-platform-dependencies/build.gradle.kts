plugins {
  id("java-library")
}

dependencies {
  api(platform(project(":gradle-platform-dependencies")))

  api("net.bytebuddy:byte-buddy")
  api("org.apache.groovy:groovy-all")
  api("org.junit.jupiter:junit-jupiter")
  api("org.junit.platform:junit-platform-launcher")
  api("org.mockito:mockito-junit-jupiter")
  api("org.spockframework:spock-core") {
    exclude(group = "org.apache.groovy", module = "groovy")
  }
  api("ch.qos.logback:logback-classic")
  api("ch.qos.logback:logback-core")
  api("org.slf4j:jcl-over-slf4j")
  api("org.slf4j:jul-to-slf4j")
  api("org.slf4j:log4j-over-slf4j")
  api("uk.org.lidalia:sysout-over-slf4j")
  api("net.logstash.logback:logstash-logback-encoder")
}
