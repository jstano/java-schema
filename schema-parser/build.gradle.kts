import com.stano.buildlogic.configurePublishing
import com.stano.buildlogic.getFullDependency

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-parser",
  description = "A parser that can read schema xml files and generate a virtual model for relational database schemas.",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  implementation(project(":schema-model"))

  implementation(getFullDependency("org.apache.commons:commons-lang3"))
  implementation(getFullDependency("org.slf4j:slf4j-api"))

  testImplementation(project(":test-platform-dependencies"))
}
