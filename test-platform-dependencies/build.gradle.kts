import com.stano.buildlogic.getFullDependency

plugins {
  id("java-library")
}

dependencies {
  api(getFullDependency("net.bytebuddy:byte-buddy"))
  api(getFullDependency("org.apache.groovy:groovy-all"))
  api(getFullDependency("org.junit.jupiter:junit-jupiter"))
  api(getFullDependency("org.junit.platform:junit-platform-launcher"))
  api(getFullDependency("org.mockito:mockito-junit-jupiter"))
  api(getFullDependency("org.spockframework:spock-core")) {
    exclude(group = "org.apache.groovy", module = "groovy")
  }
  api(getFullDependency("ch.qos.logback:logback-classic"))
  api(getFullDependency("ch.qos.logback:logback-core"))
  api(getFullDependency("org.slf4j:jcl-over-slf4j"))
  api(getFullDependency("org.slf4j:jul-to-slf4j"))
  api(getFullDependency("org.slf4j:log4j-over-slf4j"))
  api(getFullDependency("uk.org.lidalia:sysout-over-slf4j"))
  api(getFullDependency("net.logstash.logback:logstash-logback-encoder"))
}
