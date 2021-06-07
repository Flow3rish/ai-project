package cz.chrubasik.aiproject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FuzzyNumberTest {

	@Test
	void testCreation() {
		new FuzzyNumber(1D, 2D, 3D, 4D);
		new FuzzyNumber(1D, 2D, 4D);
		assertThrows(RuntimeException.class, () -> new FuzzyNumber(2D, 5D, 4D));
	}
	
	@Test
	void testGetMemberShipDegree() {
		FuzzyNumber certain = new FuzzyNumber(2D);
		FuzzyNumber trapezoidal = new FuzzyNumber(1D, 2D, 3D, 4D);
		FuzzyNumber triangular = new FuzzyNumber(1D, 2D, 4D);
	
		
		assertTrue(certain.getMembershipDegreeOfElement(1.5D).equals(FuzzyValue.FV_0));
		assertTrue(certain.getMembershipDegreeOfElement(2D).equals(FuzzyValue.FV_1));
		
		assertTrue(trapezoidal.getMembershipDegreeOfElement(1.5D).compareTo(FuzzyValue.FV_1) < 0);
		assertTrue(trapezoidal.getMembershipDegreeOfElement(1.5D).compareTo(FuzzyValue.FV_0) > 0);
		assertTrue(trapezoidal.getMembershipDegreeOfElement(2.5D).equals(FuzzyValue.FV_1));
		assertTrue(trapezoidal.getMembershipDegreeOfElement(3.5D).compareTo(FuzzyValue.FV_1) < 0);
		assertTrue(trapezoidal.getMembershipDegreeOfElement(3.5D).compareTo(FuzzyValue.FV_0) > 0);
		assertTrue(trapezoidal.getMembershipDegreeOfElement(5D).equals(FuzzyValue.FV_0));
		
		assertTrue(triangular.getMembershipDegreeOfElement(1D).equals(FuzzyValue.FV_0));
		assertTrue(triangular.getMembershipDegreeOfElement(1.5D).compareTo(FuzzyValue.FV_1) < 0);
		assertTrue(triangular.getMembershipDegreeOfElement(1.5D).compareTo(FuzzyValue.FV_0) > 0);
		assertTrue(triangular.getMembershipDegreeOfElement(2D).equals(FuzzyValue.FV_1));
		assertTrue(triangular.getMembershipDegreeOfElement(3D).compareTo(FuzzyValue.FV_1) < 0);
		assertTrue(triangular.getMembershipDegreeOfElement(3D).compareTo(FuzzyValue.FV_0) > 0);
		
		
	}

}
