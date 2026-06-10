import com.stano.buildlogic.configurePublishing

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-parser",
  description = "A parser that can read schema xml files and generate a virtual model for relational database schemas.",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  api(platform(project(":schema-platform-dependencies")))
  api(project(":schema-model"))

  implementation("org.apache.commons:commons-lang3")
  implementation("org.slf4j:slf4j-api")

  testImplementation(project(":test-platform-dependencies"))
}
