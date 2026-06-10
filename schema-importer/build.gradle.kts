import com.stano.buildlogic.configurePublishing

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-importer",
  description = "Creates schema xml files from relational database schemas.",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  api(platform(project(":schema-platform-dependencies")))
  api(project(":schema-model"))

  implementation("commons-cli:commons-cli")
  implementation("org.apache.commons:commons-collections4")
  implementation("org.apache.commons:commons-lang3")
  implementation("org.apache.commons:commons-text")
  implementation("org.slf4j:slf4j-api")
  implementation("org.postgresql:postgresql")

  testImplementation(project(":test-platform-dependencies"))
}
