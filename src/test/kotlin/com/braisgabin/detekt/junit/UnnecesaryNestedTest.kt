package com.braisgabin.detekt.junit

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class UnnecesaryNestedTest(private val env: KotlinCoreEnvironment) {

  @Test
  fun `reports an empty Nested`() {
    val code = """
      import org.junit.jupiter.api.Test
      import org.junit.jupiter.api.Nested

      class A {
        @Nested
        class B {
        }

        @Test
        fun test() {
        }
      }
      """

    val findings = UnnecesaryNested(Config.empty).compileAndLintWithContext(env, code)
    assertThat(findings).hasSize(1)
  }

  @Test
  fun `reports a top level class with @Nested`() {
    val code = """
      import org.junit.jupiter.api.Test
      import org.junit.jupiter.api.Nested

      @Nested
      class A {

        @Test
        fun test() {
        }
      }
      """

    val findings = UnnecesaryNested(Config.empty).compileAndLintWithContext(env, code)
    assertThat(findings).hasSize(1)
  }

  @Test
  fun `doesn't report`() {
    val code = """
      import org.junit.jupiter.api.Test
      import org.junit.jupiter.api.Nested

      class A {
        @Nested
        class B {
          @Test
          fun test() {
          }
        }

        @Test
        fun test() {
        }
      }
      """

    val findings = UnnecesaryNested(Config.empty).compileAndLintWithContext(env, code)
    assertThat(findings).isEmpty()
  }
}
