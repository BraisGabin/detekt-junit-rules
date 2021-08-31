package com.github.braisgabin.detektjunitrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class JunitRuleSetProvider : RuleSetProvider {
  override val ruleSetId: String = "Junit"

  override fun instance(config: Config): RuleSet {
    return RuleSet(
      ruleSetId,
      listOf(
        TestFunctionsShouldReturnUnit(config),
      ),
    )
  }
}
