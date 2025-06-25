plugins {
  id("maven-publish")
  id("signing")
}

dependencies {
  implementation(project(":schema-model"))
  implementation(project(":schema-parser"))

  implementation("commons-cli:commons-cli")
  implementation("commons-io:commons-io")
  implementation("org.apache.commons:commons-lang3")
  implementation("org.slf4j:slf4j-api")

  testImplementation("org.apache.groovy:groovy-all")
  testImplementation("net.bytebuddy:byte-buddy")
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.junit.platform:junit-platform-launcher")
  testImplementation("org.mockito:mockito-junit-jupiter")
  testImplementation("org.spockframework:spock-core")
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])

      pom {
        name.set("Schema SQL Generator")
        description.set("Generates SQL scripts for relational database schemas from the schema model.")
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
