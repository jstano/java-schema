plugins {
  id("maven-publish")
  id("signing")
}

dependencies {
  compileOnly(platform(project(":gradle-platform-dependencies")))

  implementation(project(":schema-model"))
  implementation(project(":schema-parser"))

  implementation("commons-cli:commons-cli:1.9.0")
  implementation("commons-io:commons-io:2.19.0")
  implementation("org.apache.commons:commons-lang3:3.17.0")
  implementation("org.slf4j:slf4j-api:2.0.17")

  testImplementation(project(":test-platform-dependencies"))
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
            name.set("MIT License")
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
      url = uri(layout.buildDirectory.dir("staging-deploy").get().toString())
    }
  }
}

signing {
  sign(publishing.publications["mavenJava"])
}

tasks.register<Zip>("zipStagingDeploy") {
  archiveFileName.set("staging-deploy.zip")
  destinationDirectory.set(layout.buildDirectory.dir("tmp"))
  from("build/staging-deploy") {
    include("**/*")
  }
}
