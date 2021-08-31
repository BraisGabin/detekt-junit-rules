package com.github.braisgabin.detektjunitrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.fqNameOrNull
import io.gitlab.arturbosch.detekt.rules.hasAnnotation
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getType
import org.jetbrains.kotlin.types.typeUtil.isUnit

class TestFunctionsShouldReturnUnit(config: Config) : Rule(config) {
  override val issue = Issue(
    javaClass.simpleName,
    Severity.CodeSmell,
    "The functions annotated with @Test should return Unit",
    Debt.FIVE_MINS,
  )

  override fun visitNamedFunction(function: KtNamedFunction) {
    super.visitNamedFunction(function)
    if (BindingContext.EMPTY == bindingContext) {
      return
    }

    if (function.hasAnnotation("Test") && function.returnsUnit() == false) {
      report(
        CodeSmell(
          issue,
          Entity.atName(function),
          "The function ${function.name.orEmpty()} should return Unit",
        )
      )
    }
  }

  private fun KtNamedFunction.returnsUnit(): Boolean? {
    val descriptor = bindingContext[BindingContext.DECLARATION_TO_DESCRIPTOR, this] as? FunctionDescriptor
    return descriptor?.returnType?.isUnit()
  }
}
