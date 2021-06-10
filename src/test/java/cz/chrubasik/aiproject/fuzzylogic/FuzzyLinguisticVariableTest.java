package cz.chrubasik.aiproject.fuzzylogic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import cz.chrubasik.aiproject.fuzzysets.FuzzyElementDouble;
import cz.chrubasik.aiproject.fuzzysets.FuzzySetRealsLinearContinuous;

class FuzzyLinguisticVariableTest {

	@Test
	void test() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(0D, 1D),
				new FuzzyElementDouble(4D, 1D),
				new FuzzyElementDouble(7D, 0.0D)
				).stream().collect(Collectors.toSet()));
		HashMap<String, FuzzySetRealsLinearContinuous> m_x = new HashMap<>();
		m_x.put("ledova voda", fuzzySet);
		m_x.put("studena voda", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(9D, 15D, 2D));
		m_x.put("vlazna voda", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(18D, 26D, 2D));
		m_x.put("tepla voda", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(30D, 37D, 2D));
		m_x.put("horka voda", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(40D, 100D, 2D));
		FuzzyLinguisticVariable fuzzyLinguisticVariable = new FuzzyLinguisticVariable("teplota vody v laznich", m_x);
		
	}

}
