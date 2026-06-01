import com.stano.buildlogic.configurePublishing
import com.stano.buildlogic.getFullDependency

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-installer-flyway",
  description = "Flyway schema installer",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  api(project(":schema-installer"))
  api(project(":schema-migrations"))
  api(project(":schema-model"))

  implementation(getFullDependency("com.stano:java-utils"))
  implementation(getFullDependency("com.stano:jdbc-utils"))
  implementation(getFullDependency("org.flywaydb:flyway-core"))
  implementation(getFullDependency("org.flywaydb:flyway-sqlserver"))
  implementation(getFullDependency("org.slf4j:slf4j-api"))

  testImplementation(project(":test-platform-dependencies"))
}
