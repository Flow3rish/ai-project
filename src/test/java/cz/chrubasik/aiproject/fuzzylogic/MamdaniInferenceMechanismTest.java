package cz.chrubasik.aiproject.fuzzylogic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import cz.chrubasik.aiproject.fuzzylogic.Rule.OperatorType;
import cz.chrubasik.aiproject.fuzzysets.FuzzySetRealsLinearContinuous;


class MamdaniInferenceMechanismTest {

	@Test
	void test() {
		
		HashMap<String, FuzzySetRealsLinearContinuous> m_x_1 = new HashMap<>();
		m_x_1.put("ledova_voda", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 4D, 7D));
		m_x_1.put("studena_voda", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(9D, 15D, 2D));
		m_x_1.put("vlazna_voda", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(18D, 26D, 2D));
		m_x_1.put("tepla_voda", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(30D, 37D, 2D));
		m_x_1.put("horka_voda", FuzzySetRealsLinearContinuous.ofRightCorner(38D, 42D, 100D));
		FuzzyLinguisticVariable teplota_vody = new FuzzyLinguisticVariable("teplota_vody", m_x_1);
		
		
		HashMap<String, FuzzySetRealsLinearContinuous> m_x_2 = new HashMap<>();
		m_x_2.put("nespokojeny_zakaznik", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 4D, 7D));
		m_x_2.put("indiferentni_zakaznik", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(9D, 15D, 2D));
		m_x_2.put("spokojeny_zakaznik", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(18D, 26D, 2D));
		FuzzyLinguisticVariable spokojenost_zakaznika = new FuzzyLinguisticVariable("spokojenost_zakaznika", m_x_2);
		
		Rule rule1 = Rule.builder()
				.antecedent("teplota_vody is studena_voda", "teplota_vody is ledova_voda")
				.operatorType(OperatorType.OR)
				.consequent("spokojenost_zakaznika is nespokojeny_zakaznik")
				.build();
		
		Rule rule2 = Rule.builder()
				.antecedent("teplota_vody is vlazna_voda")
				.operatorType(OperatorType.OR)
				.consequent("spokojenost_zakaznika is indiferentni_zakaznik")
				.build();
		
		Rule rule3 = Rule.builder()
				.antecedent("teplota_vody is tepla_voda")
				.operatorType(OperatorType.OR)
				.consequent("spokojenost_zakaznika is spokojeny_zakaznik")
				.build();
		
		Rule rule4 = Rule.builder()
				.antecedent("teplota_vody is horka_voda")
				.operatorType(OperatorType.OR)
				.consequent("spokojenost_zakaznika is nespokojeny_zakaznik")
				.build();
		
	
		MamdaniInferenceMechanism mamdaniInferenceMechanism = new MamdaniInferenceMechanism();
		mamdaniInferenceMechanism
				.addLinguisticVariable(teplota_vody)
				.addLinguisticVariable(spokojenost_zakaznika)
				.addRule(rule1)
				.addRule(rule2)
				.addRule(rule3)
				.addRule(rule4)
				.addMeasurement("teplota_vody", 20D);
		
		mamdaniInferenceMechanism.runInference();
		assertEquals(12D, mamdaniInferenceMechanism.defuzzifyCOA().get("spokojenost_zakaznika").getElement());
		
		
		
	}
}
