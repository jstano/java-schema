import com.stano.buildlogic.configurePublishing
import com.stano.buildlogic.getFullDependency

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-installer-liquibase",
  description = "Liquibase schema installer",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  implementation(project(":schema-installer"))
  implementation(project(":schema-migrations"))
  implementation(project(":schema-model"))
  implementation(project(":schema-parser"))
  implementation(project(":schema-sql-generator"))

  implementation(getFullDependency("com.stano:java-utils"))
  implementation(getFullDependency("com.stano:jdbc-utils"))
  implementation(getFullDependency("commons-cli:commons-cli"))
  implementation(getFullDependency("org.apache.commons:commons-lang3"))
  implementation(getFullDependency("org.apache.commons:commons-collections4"))
  implementation(getFullDependency("org.slf4j:slf4j-api"))
  implementation(getFullDependency("org.postgresql:postgresql"))
  implementation(getFullDependency("org.liquibase:liquibase-core"))

  testImplementation(project(":test-platform-dependencies"))
}
