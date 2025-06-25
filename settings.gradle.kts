rootProject.name = "schema"

dependencyResolutionManagement {
  repositories {
    mavenLocal()
    mavenCentral()
  }
}

include("schema-bom")
include("schema-importer")
include("schema-installer")
include("schema-installer-liquibase")
include("schema-migrations")
include("schema-model")
include("schema-parser")
include("schema-platform")
include("schema-sql-generator")
