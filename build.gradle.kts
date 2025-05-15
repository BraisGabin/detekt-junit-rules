import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "2.1.21"
}

buildscript {
  dependencies {
    classpath("com.vanniktech:gradle-maven-publish-plugin:0.32.0")
  }
}

apply(plugin="com.vanniktech.maven.publish")

dependencies {
  compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.8")

  testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.23.8")
  testImplementation("io.gitlab.arturbosch.detekt:detekt-test-utils:1.23.8")
  testImplementation("org.assertj:assertj-core:3.27.3")
  testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  jvmToolchain(11)
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
  systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
  val compileSnippetText: Boolean = project.hasProperty("compile-test-snippets")
  systemProperty("compile-snippet-tests", compileSnippetText)
}
