package com.braisgabin.detekt.junit

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.hasAnnotation
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.siblings

class UnnecessaryNested(config: Config) : Rule(config) {
  override val issue = Issue(
    javaClass.simpleName,
    Severity.CodeSmell,
    "A @Nested class should always contain at least one @Test or more than one @Nested. And class shouldn't only contain one @Nested",
    Debt.FIVE_MINS,
  )

  override fun visitClassOrObject(classOrObject: KtClassOrObject) {
    super.visitClassOrObject(classOrObject)

    if (classOrObject.hasAnnotation("Nested")) {
      if (classOrObject.parent is KtFile) {
        report(
          CodeSmell(
            issue,
            Entity.atName(classOrObject),
            "The class ${classOrObject.name.orEmpty()} is top-level so it doesn't need to be annotated with `@Nested`.",
          )
        )
        return
      }

      val body = classOrObject.body
      if (body?.declarations.orEmpty().none { it.hasAnnotation("Test", "Nested", "ParameterizedTest", "TestFactory") }) {
        report(
          CodeSmell(
            issue,
            Entity.atName(classOrObject),
            "The class ${classOrObject.name.orEmpty()} is empty so it doesn't need to be annotated with `@Nested`.",
          )
        )
        return
      }

      if (classOrObject.allKtDeclarationSiblings().none { it.hasAnnotation("Test", "Nested", "ParameterizedTest", "TestFactory") }) {
        report(
          CodeSmell(
            issue,
            Entity.atName(classOrObject),
            "The class ${classOrObject.name.orEmpty()} could be removed because it is the only child inside its parent`.",
          )
        )
        return
      }
    }
  }
}

private fun PsiElement.allKtDeclarationSiblings(): Sequence<KtDeclaration> {
  return (siblings(forward = false, withItself = false) + siblings(forward = true, withItself = false))
    .mapNotNull { it as? KtDeclaration }
}
