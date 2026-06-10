rootProject.name = "schema"

dependencyResolutionManagement {
  repositories {
    mavenLocal()
    mavenCentral()
  }
}

include("schema-bom")
include("schema-diagram-generator")
include("schema-importer")
include("schema-installer")
include("schema-installer-flyway")
include("schema-installer-liquibase")
include("schema-migrations")
include("schema-model")
include("schema-parser")
include("schema-platform-dependencies")
include("schema-sql-generator")
include("test-platform-dependencies")
