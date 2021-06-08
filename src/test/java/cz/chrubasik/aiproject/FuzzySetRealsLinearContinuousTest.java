package cz.chrubasik.aiproject;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;


class FuzzySetRealsLinearContinuousTest {

	@Test
	void testGetMembershipDegreeOfElement() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
	
		assertEquals(FuzzySetRealsLinearContinuous.FuzzySetType.TRAPEZOIDAL, fuzzySet.getFuzzySetType());
		assertEquals(FuzzyValue.of(0D), fuzzySet.getMembershipDegreeOfElement(-3D));
		assertEquals(FuzzyValue.of(0.5D), fuzzySet.getMembershipDegreeOfElement(1D));
		assertEquals(FuzzyValue.of(0.7D), fuzzySet.getMembershipDegreeOfElement(2D));
		assertEquals(FuzzyValue.of(0.9D), fuzzySet.getMembershipDegreeOfElement(3D));
		assertEquals(FuzzyValue.of(0.5D), fuzzySet.getMembershipDegreeOfElement(5D));
		assertEquals(FuzzyValue.of(0D), fuzzySet.getMembershipDegreeOfElement(100D));
		
		assertTrue(fuzzySet.getMembershipDegreeOfElement(1.1D).compareTo(fuzzySet.getMembershipDegreeOfElement(1.2D)) < 0);
		assertTrue(fuzzySet.getMembershipDegreeOfElement(3.3D).compareTo(fuzzySet.getMembershipDegreeOfElement(5D)) > 0);
		
		
	}

}
