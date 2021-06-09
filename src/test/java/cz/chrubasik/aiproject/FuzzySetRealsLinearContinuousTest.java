package cz.chrubasik.aiproject;

import static org.junit.jupiter.api.Assertions.*;

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
	
	@Test
	void testOrderingInGetElements() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		List<FuzzyElementDouble> elements = fuzzySet.getElements();
		assertEquals(new FuzzyElementDouble(1D, 0.5D), elements.get(0));
		assertEquals(new FuzzyElementDouble(5D, 0.5D), elements.get(3));
		
	}
	
	@Test
	void testMuWholeSet() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		fuzzySet.getElements().forEach(el -> assertEquals(fuzzySet.muCOnWholeSet(el.getElement()), el.getMembershipDegree()));
		assertEquals(fuzzySet.muCOnWholeSet(1.5D), FuzzyValue.of(0.6D));
		assertEquals(fuzzySet.muCOnWholeSet(4.5D), FuzzyValue.of(0.6D));
	}
	
	@Test
	void testLineIntersect_WhenIntersecting() {
		FuzzyElementDouble e1 = new FuzzyElementDouble(1D, 0D);
		FuzzyElementDouble e2 = new FuzzyElementDouble(5D, 1D);
		FuzzyElementDouble e3 = new FuzzyElementDouble(0D, 1D);
		FuzzyElementDouble e4 = new FuzzyElementDouble(6D, 0D);
		FuzzyElementDouble intersect = FuzzySetRealsLinearContinuous._twoLineIntersection(e1, e2, e3, e4);
		assertNotNull(intersect);
	}
	
	@Test
	void testLineIntersect_WhenNotIntersecting() {
		FuzzyElementDouble e1 = new FuzzyElementDouble(0D, 0D);
		FuzzyElementDouble e2 = new FuzzyElementDouble(1D, 1D);
		FuzzyElementDouble e3 = new FuzzyElementDouble(2D, 0D);
		FuzzyElementDouble e4 = new FuzzyElementDouble(3D, 1D);
		FuzzyElementDouble intersect = FuzzySetRealsLinearContinuous._twoLineIntersection(e1, e2, e3, e4);
		assertNull(intersect);
	}
	
	@Test
	void testLineIntersect_WhenBorderPointInCommon() {
		FuzzyElementDouble e1 = new FuzzyElementDouble(0D, 0D);
		FuzzyElementDouble e2 = new FuzzyElementDouble(1D, 1D);
		FuzzyElementDouble e3 = new FuzzyElementDouble(0D, 0D);
		FuzzyElementDouble e4 = new FuzzyElementDouble(3D, 1D);
		FuzzyElementDouble intersect = FuzzySetRealsLinearContinuous._twoLineIntersection(e1, e2, e3, e4);
		assertNull(intersect);
	}
	
	@Test
	void testLineIntersect_WhenSameDirectionSameLength() {
		FuzzyElementDouble e1 = new FuzzyElementDouble(0D, 0D);
		FuzzyElementDouble e2 = new FuzzyElementDouble(1D, 1D);
		FuzzyElementDouble e3 = new FuzzyElementDouble(0D, 0D);
		FuzzyElementDouble e4 = new FuzzyElementDouble(1D, 1D);
		FuzzyElementDouble intersect = FuzzySetRealsLinearContinuous._twoLineIntersection(e1, e2, e3, e4);
		assertNull(intersect);
	}
	
	@Test
	void testLineIntersect_WhenSameDirectionDifferentLength() {
		FuzzyElementDouble e1 = new FuzzyElementDouble(0D, 0D);
		FuzzyElementDouble e2 = new FuzzyElementDouble(1D, 1D);
		FuzzyElementDouble e3 = new FuzzyElementDouble(0D, 0D);
		FuzzyElementDouble e4 = new FuzzyElementDouble(.5D, .5D);
		FuzzyElementDouble intersect = FuzzySetRealsLinearContinuous._twoLineIntersection(e1, e2, e3, e4);
		assertNull(intersect);
	}
	
	
	
	
	
	@Test
	void testCeil_WhenPureIntersect() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySetRealsLinearContinuous ceiledSet = fuzzySet.ceilSet(FuzzyValue.of(0.6D));
		assertEquals(1.5D, ceiledSet.getElements().get(1).getElement());
	}
	
	@Test
	void testCeil_WhenBelowAll() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySetRealsLinearContinuous ceiledSet = fuzzySet.ceilSet(FuzzyValue.of(0.4));
		assertEquals(0, ceiledSet.getElements().size());
	}
	
	@Test
	void testCeil_WhenAboveAll() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySetRealsLinearContinuous ceiledSet = fuzzySet.ceilSet(FuzzyValue.of(1D));
		assertEquals(fuzzySet.size(), ceiledSet.size());
	}
	
	@Test
	void testCeil_WhenTouchingPeak() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySetRealsLinearContinuous ceiledSet = fuzzySet.ceilSet(FuzzyValue.of(0.9D));
		assertEquals(fuzzySet.size(), ceiledSet.size());
	}
	
	@Test
	void testCeil_WhenIntersectingAPoint() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySetRealsLinearContinuous ceiledSet = fuzzySet.ceilSet(FuzzyValue.of(0.5D));
		assertEquals(2, ceiledSet.size());
		ceiledSet = fuzzySet.ceilSet(FuzzyValue.of(0.8D));
		assertEquals(FuzzyValue.of(0.8D), ceiledSet.getElements().get(2).getMembershipDegree());
	}
	
	
	@Test void testUnitonOneIntersection() {
		FuzzySetRealsLinearContinuous fuzzySet1 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySetRealsLinearContinuous fuzzySet2 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.8D),
				new FuzzyElementDouble(3D, 0.8D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySetRealsLinearContinuous unitedSet = fuzzySet1.union(fuzzySet2);
		assertEquals(5, unitedSet.size());
	}
	
	@Test void testUnitonSameSets() {
		FuzzySetRealsLinearContinuous fuzzySet1 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySetRealsLinearContinuous fuzzySet2 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySetRealsLinearContinuous unitedSet = fuzzySet1.union(fuzzySet2);
		assertEquals(fuzzySet1, unitedSet);
		assertEquals(fuzzySet2, unitedSet);
	}
	
	@Test void testUnitonNoLineIntersect() {
		FuzzySetRealsLinearContinuous fuzzySet1 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySetRealsLinearContinuous fuzzySet2 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.4D),
				new FuzzyElementDouble(2D, 0.6D),
				new FuzzyElementDouble(3D, 0.8D),
				new FuzzyElementDouble(5D, 0.4D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySetRealsLinearContinuous unitedSet = fuzzySet1.union(fuzzySet2);
		assertEquals(fuzzySet1, unitedSet);
	}
	
	
	@Test
	void testIntersection() {
		FuzzySetRealsLinearContinuous fuzzySet1 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySetRealsLinearContinuous fuzzySet2 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.4D),
				new FuzzyElementDouble(2D, 0.6D),
				new FuzzyElementDouble(3D, 0.8D),
				new FuzzyElementDouble(5D, 0.4D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySetRealsLinearContinuous unitedSet = fuzzySet1.intersection(fuzzySet2);
		assertEquals(fuzzySet2, unitedSet);
	}
	
	@Test
	void testComplement() {
		FuzzySetRealsLinearContinuous fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySetRealsLinearContinuous fuzzySetComplement = fuzzySet.complement();
		assertEquals(FuzzyValue.of(0.3), fuzzySetComplement.getElements().get(1).getMembershipDegree());
		
	}


}
