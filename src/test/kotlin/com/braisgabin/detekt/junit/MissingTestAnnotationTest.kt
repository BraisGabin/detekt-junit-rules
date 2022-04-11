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

  @Test
  fun `don't report functions annotated with BeforeClass, Before, After or AfterClass Junit4`() {
    val code = """
      import org.junit.After
      import org.junit.AfterClass
      import org.junit.Before
      import org.junit.BeforeClass
      import org.junit.Test

      class A {
        @BeforeClass
        fun setUpClass() {
        }

        @Before
        fun setUp() {
        }

        @After
        fun tearDown() {
        }

        @AfterClass
        fun tearDownClass() {
        }

        @Test
        fun test() {
        }
      }
      """

    val findings = MissingTestAnnotation(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }

  @Test
  fun `don't report functions annotated with BeforeAll, BeforeEach, AfterEach, AfterAll or ParameterizedTest Junit5`() {
    val code = """
      import org.junit.jupiter.api.AfterAll
      import org.junit.jupiter.api.AfterEach
      import org.junit.jupiter.api.BeforeAll
      import org.junit.jupiter.api.BeforeEach
      import org.junit.jupiter.api.Test
      import org.junit.jupiter.params.ParameterizedTest

      class A {
        @BeforeAll
        fun setUpClass() {
        }

        @BeforeEach
        fun setUp() {
        }

        @AfterEach
        fun tearDown() {
        }

        @AfterAll
        fun tearDownClass() {
        }

        @Test
        fun test() {
        }

        @ParameterizedTest
        fun test2() {
        }
      }
      """

    val findings = MissingTestAnnotation(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }

  @Test
  fun `don't report override functions`() {
    val code = """
      import org.junit.jupiter.api.extension.ExtensionContext
      import org.junit.jupiter.params.provider.Arguments
      import org.junit.jupiter.params.provider.ArgumentsProvider
      import java.util.stream.Stream

      class A : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
          TODO()
        }
      }
      """

    val findings = MissingTestAnnotation(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }


  @Test
  fun `don't report functions in private classes without @Test`() {
    val code = """
      import org.junit.Test

      class A {
        @Test
        fun test() {
        }

        private class B {
          private fun test2() {
          }
        }
      }

      private class C {
        private fun test3() {
        }
      }
      """

    val findings = MissingTestAnnotation(Config.empty).compileAndLintWithContext(env.env, code)
    assertThat(findings).isEmpty()
  }
}
