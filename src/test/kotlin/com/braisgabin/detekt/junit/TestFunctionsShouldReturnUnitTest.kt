package com.braisgabin.detekt.junit

import io.github.detekt.test.utils.KotlinCoreEnvironmentWrapper
import io.github.detekt.test.utils.createEnvironment
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class TestFunctionsShouldReturnUnitTest {

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
  fun `don't report test function without defined Unit return type`() {
    val code = """
      import org.junit.Test
      
      class A {
        @Test
        fun test() {
          return Unit
        }
      }
      """

    val findings = TestFunctionsShouldReturnUnit(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }

  @Test
  fun `Report test function without defined not Unit return type`() {
    val code = """
      import org.junit.Test
      
      class A {
        @Test
        fun test() = 5
      }
      """

    val findings = TestFunctionsShouldReturnUnit(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).hasSize(1)
  }

  @Test
  fun `don't test report function with defined Unit return type`() {
    val code = """
      import org.junit.Test
      
      class A {
        @Test
        fun test(): Unit {
        }
      }
      """

    val findings = TestFunctionsShouldReturnUnit(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }

  @Test
  fun `report test function with defined not unit return type`() {
    val code = """
      import org.junit.Test
      
      class A {
        @Test
        fun test(): Int {
          return 5
        }
      }
      """

    val findings = TestFunctionsShouldReturnUnit(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).hasSize(1)
  }

  @Test
  fun `report test function with not Unit implicit return type`() {
    val code = """
      import org.junit.Test
      
      class A {
        @Test
        fun test() = same(5)
      }

      fun <T> same(value: T): T = value
      """

    val findings = TestFunctionsShouldReturnUnit(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).hasSize(1)
  }

  @Test
  fun `don't report test function with Unit implicit return type`() {
    val code = """
      import org.junit.Test
      
      class A {
        @Test
        fun test() = same(Unit)
      }

      fun <T> same(value: T): T = value
      """

    val findings = TestFunctionsShouldReturnUnit(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }

  @Test
  fun `report test function using runBlocking returning not Unit type`() {
    val code = """
      import org.junit.Test
      import kotlinx.coroutines.runBlocking
      
      class A {
        @Test
        fun test() = runBlocking { 5 }
      }
      """

    val findings = TestFunctionsShouldReturnUnit(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).hasSize(1)
  }

  @Test
  fun `don't report test function using runBlocking returning Unit`() {
    val code = """
      import org.junit.Test
      import kotlinx.coroutines.runBlocking
      
      class A {
        @Test
        fun test() = runBlocking { Unit }
      }
      """

    val findings = TestFunctionsShouldReturnUnit(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }

  @Test
  fun `don't report no test function`() {
    val code = """
      class A {
        fun test() = 5
      }
      """

    val findings = TestFunctionsShouldReturnUnit(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }
}
