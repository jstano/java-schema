import com.stano.buildlogic.configurePublishing
import com.stano.buildlogic.getFullDependency

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-importer",
  description = "Creates schema xml files from relational database schemas.",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  api(project(":schema-model"))

  implementation(getFullDependency("commons-cli:commons-cli"))
  implementation(getFullDependency("org.apache.commons:commons-collections4"))
  implementation(getFullDependency("org.apache.commons:commons-lang3"))
  implementation(getFullDependency("org.apache.commons:commons-text"))
  implementation(getFullDependency("org.slf4j:slf4j-api"))
  implementation(getFullDependency("org.postgresql:postgresql"))

  testImplementation(project(":test-platform-dependencies"))
}
