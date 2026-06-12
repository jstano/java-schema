package com.stano.buildlogic

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.external.javadoc.CoreJavadocOptions
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.tasks.JacocoReport

class JavaLibraryConventionPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    with(project) {
    plugins.apply("java-library")
    plugins.apply("jacoco")
    plugins.apply("maven-publish")
    plugins.apply("signing")
    plugins.apply("com.diffplug.spotless")

    configurations.all {
      exclude(group = "commons-logging", module = "commons-logging")
    }

    extensions.configure<JavaPluginExtension> {
      withSourcesJar()
      withJavadocJar()
    }

    tasks.withType<JavaCompile>().configureEach {
      options.compilerArgs = defaultCompilerOptions()
      sourceCompatibility = "21"
      targetCompatibility = "21"
    }

    tasks.withType<Jar>().configureEach {
      exclude("**/.gitkeep")
    }

    tasks.withType<Javadoc>().configureEach {
      (options as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    tasks.withType<Test>().configureEach {
      useJUnitPlatform()
      jvmArgs = listOf(
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang=ALL-UNNAMED"
      )
      finalizedBy("jacocoTestReport")
      testLogging {
        events = setOf(TestLogEvent.FAILED, TestLogEvent.SKIPPED)
      }
    }

    tasks.withType<JacocoReport>().configureEach {
      dependsOn("test")
      reports {
        html.required.set(true)
        xml.required.set(true)
      }
    }

    extensions.configure<SpotlessExtension> {
      java {
        googleJavaFormat("1.35.0")
          .reflowLongStrings()
          .formatJavadoc(true)
        endWithNewline()
        importOrder()
        removeUnusedImports()
        trimTrailingWhitespace()
      }
    }

    tasks.named("check") {
      dependsOn("spotlessCheck")
    }
    }
  }

  private fun defaultCompilerOptions() = listOf(
    "-Xlint:unchecked",
    "-Xlint:deprecation",
    "-parameters"
  )
}
