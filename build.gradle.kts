import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.20"
  `maven-publish`
}

group = "com.braisgabin.detekt"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.20.0")

  testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.18.0")
  testImplementation("io.gitlab.arturbosch.detekt:detekt-test-utils:1.18.0")
  testImplementation("org.assertj:assertj-core:3.22.0")
  testImplementation("junit:junit:4.13.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test>().configureEach {
  val compileSnippetText: Boolean = if (project.hasProperty("compile-test-snippets")) {
    (project.property("compile-test-snippets") as String).toBoolean()
  } else {
    false
  }
  systemProperty("compile-snippet-tests", compileSnippetText)
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
  }
}
