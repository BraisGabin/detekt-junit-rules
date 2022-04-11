package com.braisgabin.detekt.junit

import io.github.detekt.test.utils.KotlinCoreEnvironmentWrapper
import io.github.detekt.test.utils.createEnvironment
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.gitlab.arturbosch.detekt.test.lintWithContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class MissingTestAnnotationTest {

  private lateinit var env: KotlinCoreEnvironmentWrapper

  @Before
  fun setUp() {
    env = createEnvironment()
  }

  @After
  fun tearDown() {
    env.dispose()
  }

  @Test
  fun `report function without @Test`() {
    val code = """
      import org.junit.Test
      
      class A {
        @Test
        fun test() {
        }

        fun test2() {
        }
      }
      """

    val findings = MissingTestAnnotation(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).hasSize(1)
  }

  @Test
  fun `Don't report function if in the class there is no junit imports`() {
    val code = """
      class A {

        @Test
        fun test() {
        }

        fun test2() {
        }
      }
      """

    val findings = MissingTestAnnotation(Config.empty).lintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }

  @Test
  fun `don't report top level functions without @Test`() {
    val code = """
      import org.junit.Test
      
      class A {
        @Test
        fun test() {
        }
      }

      fun test2() {
      }
      """

    val findings = MissingTestAnnotation(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }


  @Test
  fun `don't report private functions without @Test`() {
    val code = """
      import org.junit.Test
      
      class A {
        @Test
        fun test() {
        }

        private fun test2() {
        }
      }
      """

    val findings = MissingTestAnnotation(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }
}
