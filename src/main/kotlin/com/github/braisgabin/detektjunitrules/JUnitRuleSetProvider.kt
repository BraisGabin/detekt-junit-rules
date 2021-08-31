package com.github.braisgabin.detektjunitrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class JUnitRuleSetProvider : RuleSetProvider {
  override val ruleSetId: String = "JUnit"

  override fun instance(config: Config): RuleSet {
    return RuleSet(
      ruleSetId,
      listOf(
        TestFunctionsShouldReturnUnit(config),
      ),
    )
  }
}
