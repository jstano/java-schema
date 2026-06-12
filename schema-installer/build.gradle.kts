import com.stano.buildlogic.configurePublishing

plugins {
  id("com.stano.java-library-convention")
}

configurePublishing(
  name = "schema-installer",
  description = "Installs a schema into a database",
  url = "https://github.com/jstano/java-schema"
)

dependencies {
  api(platform(project(":schema-platform-dependencies")))

  api(project(":schema-migrations"))
  api(project(":schema-model"))
  api(project(":schema-parser"))
  api(project(":schema-sql-generator"))

  testImplementation(project(":test-platform-dependencies"))
  testRuntimeOnly("com.h2database:h2")
}
