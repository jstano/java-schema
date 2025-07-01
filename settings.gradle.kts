rootProject.name = "schema"

dependencyResolutionManagement {
  repositories {
    mavenLocal()
    mavenCentral()
  }
}

//include("gradle-platform-dependencies")
include("schema-bom")
include("schema-importer")
include("schema-installer")
include("schema-installer-liquibase")
include("schema-migrations")
include("schema-model")
include("schema-parser")
include("schema-sql-generator")
include("test-platform-dependencies")
