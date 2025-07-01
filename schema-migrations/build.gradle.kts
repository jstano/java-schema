import com.stano.buildlogic.configurePublishing
import com.stano.buildlogic.getFullDependency

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-migrations",
  description = "Migration helper classes",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  implementation(getFullDependency("com.stano:jdbc-utils"))
  implementation(getFullDependency("org.apache.commons:commons-lang3"))
  implementation(getFullDependency("org.apache.commons:commons-collections4"))
  implementation(getFullDependency("org.slf4j:slf4j-api"))
  implementation(getFullDependency("org.postgresql:postgresql"))

  testImplementation(project(":test-platform-dependencies"))
}
