import com.stano.buildlogic.configurePublishing
import com.stano.buildlogic.getFullDependency

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-diagram-generator",
  description = "Generates ER diagrams (Mermaid, PlantUML) from the schema model.",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  implementation(project(":schema-model"))
  implementation(project(":schema-parser"))

  implementation(getFullDependency("org.slf4j:slf4j-api"))

  testImplementation(project(":test-platform-dependencies"))
}
