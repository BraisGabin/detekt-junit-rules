import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.21"
  `maven-publish`
}

group = "com.braisgabin.detekt"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.20.0")

  testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.20.0")
  testImplementation("io.gitlab.arturbosch.detekt:detekt-test-utils:1.20.0")
  testImplementation("org.assertj:assertj-core:3.23.0")
  testRuntimeOnly("junit:junit:4.13.2")
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
  systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
  val compileSnippetText: Boolean = project.hasProperty("compile-test-snippets")
  systemProperty("compile-snippet-tests", compileSnippetText)
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
  }
}
