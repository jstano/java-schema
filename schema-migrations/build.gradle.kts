import com.stano.buildlogic.configurePublishing

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-migrations",
  description = "Migration helper classes",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  api(platform(project(":schema-platform-dependencies")))

  implementation("com.stano:jdbc-utils")
  implementation("org.apache.commons:commons-lang3")
  implementation("org.apache.commons:commons-collections4")
  implementation("org.slf4j:slf4j-api")
  implementation("org.postgresql:postgresql")

  testImplementation(project(":test-platform-dependencies"))
}
