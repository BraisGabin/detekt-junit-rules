package com.braisgabin.detekt.junit

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.hasAnnotation
import io.gitlab.arturbosch.detekt.rules.isOverride
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import org.jetbrains.kotlin.psi.psiUtil.isPrivate

class MissingTestAnnotation(config: Config) : Rule(config) {
  override val issue = Issue(
    javaClass.simpleName,
    Severity.CodeSmell,
    "If a file has an `org.junit` import all the non private functions inside a class should be annotated with `@Test`",
    Debt.FIVE_MINS,
  )

  override fun visitKtFile(file: KtFile) {
    if (file.importDirectives.any { it.importPath.toString().startsWith("org.junit") }) {
      super.visitKtFile(file)
    }
  }

  override fun visitNamedFunction(function: KtNamedFunction) {
    super.visitNamedFunction(function)

    val clazz = function.containingClass() ?: return

    if (
      !function.hasAnnotation(
        "Test",
        "BeforeEach",
        "AfterEach",
        "BeforeAll",
        "AfterAll",
        "ParameterizedTest",
        "Before",
        "After",
        "BeforeClass",
        "AfterClass",
        "Parameters",
      )
      && !function.isPrivate()
      && !function.isOverride()
      && !clazz.isPrivate()
    ) {
      report(
        CodeSmell(
          issue,
          Entity.atName(function),
          "Missing annotation `@Test` on the function ${function.name.orEmpty()}",
        )
      )
    }
  }
}
