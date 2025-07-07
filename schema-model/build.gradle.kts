import com.stano.buildlogic.configurePublishing
import com.stano.buildlogic.getFullDependency

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-model",
  description = "A virtual model for relational database schemas.",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  implementation(getFullDependency("org.apache.commons:commons-lang3"))
  implementation(getFullDependency("org.apache.commons:commons-collections4"))
  implementation(getFullDependency("org.slf4j:slf4j-api"))

  testImplementation(project(":test-platform-dependencies"))
}
