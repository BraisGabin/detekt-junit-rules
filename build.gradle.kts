import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.7.20"
}

buildscript {
  dependencies {
    classpath("com.vanniktech:gradle-maven-publish-plugin:0.24.0")
  }
}

apply(plugin="com.vanniktech.maven.publish")

dependencies {
  compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.22.0")

  testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.22.0")
  testImplementation("io.gitlab.arturbosch.detekt:detekt-test-utils:1.22.0")
  testImplementation("org.assertj:assertj-core:3.24.2")
  testRuntimeOnly("junit:junit:4.13.2")
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
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
