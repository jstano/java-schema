plugins {
  id("java-platform")
  id("maven-publish")
  id("signing")
}

javaPlatform {
  allowDependencies()
}

dependencies {
  api(project(":schema-importer"))
  api(project(":schema-model"))
  api(project(":schema-parser"))
  api(project(":schema-sql-generator"))
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["javaPlatform"])
      pom {
        name.set("Schema BOM")
        description.set("Maven BOM for the java-schema project.")
        url.set("https://github.com/jstano/java-schema")

        licenses {
          license {
            name.set("The MIT License")
            url.set("https://opensource.org/license/mit")
          }
        }

        developers {
          developer {
            id.set("jstano")
            name.set("Jeff Stano")
            email.set("jeff@stano.com")
          }
        }

        scm {
          connection.set("scm:git:https://github.com/jstano/java-schema.git")
          developerConnection.set("scm:git:ssh://git@github.com:jstano/java-schema.git")
          url.set("https://github.com/jstano/java-schema")
        }
      }
    }
  }

  repositories {
    maven {
      name = "sonatype"
      val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
      val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
      url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

      credentials {
        username = findProperty("ossrhUsername") as String?
        password = findProperty("ossrhPassword") as String?
      }
    }
  }
}

signing {
  useInMemoryPgpKeys(
    findProperty("signing.keyId") as String?,
    findProperty("signing.key") as String?,
    findProperty("signing.password") as String?
  )
  sign(publishing.publications["mavenJava"])
}
