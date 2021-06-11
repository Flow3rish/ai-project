package cz.chrubasik.aiproject.fuzzylogic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import cz.chrubasik.aiproject.fuzzylogic.Rule.OperatorType;

class RuleTest {

	@Test
	void test() {
		Rule rule = Rule.builder()
		.antecedent("teplota_vody is studena_voda")
		.antecedent("teplota_vody is ledova_voda")
		.operatorType(OperatorType.OR)
		.consequent("zakaznik is nespokojeny")
		.build();
		
		assertEquals("nespokojeny", rule.getConsequent().getPredicate());
	}

}
