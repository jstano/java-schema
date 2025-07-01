import com.stano.buildlogic.configurePublishing
import com.stano.buildlogic.getFullDependency

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-sql-generator",
  description = "Generates SQL scripts for relational database schemas from the schema model.",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  implementation(project(":schema-model"))
  implementation(project(":schema-parser"))

  implementation(getFullDependency("commons-cli:commons-cli"))
  implementation(getFullDependency("commons-io:commons-io"))
  implementation(getFullDependency("org.apache.commons:commons-lang3"))
  implementation(getFullDependency("org.apache.commons:commons-text"))
  implementation(getFullDependency("org.slf4j:slf4j-api"))

  testImplementation(project(":test-platform-dependencies"))
}
