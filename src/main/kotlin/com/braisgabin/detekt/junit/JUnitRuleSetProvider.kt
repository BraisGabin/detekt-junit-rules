package com.braisgabin.detekt.junit

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class JUnitRuleSetProvider : RuleSetProvider {
  override val ruleSetId: String = "JUnit"

  override fun instance(config: Config): RuleSet {
    return RuleSet(
      ruleSetId,
      listOf(
        MissingTestAnnotation(config),
        TestFunctionsShouldReturnUnit(config),
        UnnecessaryNested(config),
      ),
    )
  }
}
