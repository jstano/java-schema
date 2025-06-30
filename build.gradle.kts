plugins {
  id("java-library")
}

configure(javaProjects()) {
  apply(plugin = "java-library")
  apply(plugin = "groovy")
  apply(plugin = "jacoco")

  configurations {
    all {
      exclude(group = "commons-logging", module = "commons-logging")
    }
  }

  tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs = compilerOptions()
    sourceCompatibility = "21"
    targetCompatibility = "21"
  }
  tasks.withType<GroovyCompile>().configureEach {
    options.compilerArgs = compilerOptions()
    sourceCompatibility = "21"
    targetCompatibility = "21"
    groovyOptions.setParameters(true)
  }

  java {
    withJavadocJar()
    withSourcesJar()
  }

  tasks.withType<Jar> {
    exclude("**/.gitkeep")
  }
  tasks.withType<Javadoc>().configureEach {
    (options as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
  }
  tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED", "--add-opens", "java.base/java.lang=ALL-UNNAMED")
    finalizedBy("jacocoTestReport")
  }
  tasks.withType<JacocoReport>().configureEach {
    reports {
      html.required.set(true)
      xml.required.set(true)
    }
  }
}

fun javaProjects(): Set<Project> = subprojects - projectsToSkip()
fun projectsToSkip(): Set<Project> = subprojects.filter { project -> project.name == "schema-bom" || project.name == "gradle-platform-dependencies" }.toSet()
fun compilerOptions(): List<String> = listOf("-Xlint:none", "-Xdoclint:none", "-nowarn", "-parameters")
