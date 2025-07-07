package com.stano.buildlogic

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.signing.SigningExtension

fun Project.configurePublishing(name: String, description: String, url: String) {
  extensions.getByType<PublishingExtension>().apply {
    publications {
      create<MavenPublication>("mavenJava") {
        from(components["java"])
        pom {
          this.name.set(name)
          this.description.set(description)
          this.url.set(url)
          licenses {
            license {
              this.name.set("MIT License")
              this.url.set("https://opensource.org/license/mit")
            }
          }
          developers {
            developer {
              this.id.set("jstano")
              this.name.set("Jeff Stano")
              this.email.set("jeff@stano.com")
            }
          }
          scm {
            connection.set("scm:git:$url.git")
            developerConnection.set("scm:git:ssh://git@github.com:jstano/${project.name}.git")
            this.url.set(url)
          }
        }
      }
    }
    repositories {
      maven {
        this.url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
      }
    }
  }

  extensions.getByType<SigningExtension>().apply {
    isRequired = gradle.taskGraph.hasTask("publish")
    sign(extensions.getByType<PublishingExtension>().publications["mavenJava"])
  }

  tasks.register<Zip>("zipStagingDeploy") {
    archiveFileName.set("staging-deploy.zip")
    destinationDirectory.set(layout.buildDirectory.dir("tmp"))
    from("build/staging-deploy") {
      include("**/*")
    }
  }
}
