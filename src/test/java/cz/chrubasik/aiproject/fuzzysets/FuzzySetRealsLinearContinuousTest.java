package cz.chrubasik.aiproject.fuzzysets;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static cz.chrubasik.aiproject.fuzzysets.FuzzySetRealsLinearContinuous.FuzzySetType;



class FuzzySetRealsLinearContinuousTest {

	
	@Test
	void testOrderingInGetElements() {
		FuzzySet<FuzzyElementDouble> fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
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
		fuzzySet.getElements().forEach(el -> assertEquals(fuzzySet.mu_c(el.getElement()), el.getMembershipDegree()));
		assertEquals(fuzzySet.mu_c(1.5D), FuzzyValue.of(0.6D));
		assertEquals(fuzzySet.mu_c(4.5D), FuzzyValue.of(0.6D));
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
		FuzzySet<FuzzyElementDouble> fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySet<FuzzyElementDouble> ceiledSet = fuzzySet.ceil(FuzzyValue.of(0.6D));
		assertEquals(1.5D, ceiledSet.getElements().get(1).getElement());
	}
	
	@Test
	void testCeil_WhenBelowAll() {
		FuzzySet<FuzzyElementDouble> fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySet<FuzzyElementDouble> ceiledSet = fuzzySet.ceil(FuzzyValue.of(0.4));
		assertEquals(0, ceiledSet.getElements().size());
	}
	
	@Test
	void testCeil_WhenAboveAll() {
		FuzzySet<FuzzyElementDouble> fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySet<FuzzyElementDouble> ceiledSet = fuzzySet.ceil(FuzzyValue.of(1D));
		assertEquals(fuzzySet.size(), ceiledSet.size());
	}
	
	@Test
	void testCeil_WhenTouchingPeak() {
		FuzzySet<FuzzyElementDouble> fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySet<FuzzyElementDouble> ceiledSet = fuzzySet.ceil(FuzzyValue.of(0.9D));
		assertEquals(fuzzySet.size(), ceiledSet.size());
	}
	
	@Test
	void testCeil_WhenIntersectingAPoint() {
		FuzzySet<FuzzyElementDouble> fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySet<FuzzyElementDouble> ceiledSet = fuzzySet.ceil(FuzzyValue.of(0.5D));
		assertEquals(2, ceiledSet.size());
		ceiledSet = fuzzySet.ceil(FuzzyValue.of(0.8D));
		assertEquals(FuzzyValue.of(0.8D), ceiledSet.getElements().get(2).getMembershipDegree());
	}
	
	@Test
	void testCeil_WhenIntersectTwoElements() {
		FuzzySet<FuzzyElementDouble> fuzzySet = FuzzySetRealsLinearContinuous.ofRightCorner(5D, 8D, 10D);
		FuzzySet<FuzzyElementDouble> ceiledSet = fuzzySet.ceil(FuzzyValue.of(0.5D));
		assertEquals(2, ceiledSet.size());
		
	}
	
	
	@Test void testUnitonOneIntersection() {
		FuzzySet<FuzzyElementDouble> fuzzySet1 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySet<FuzzyElementDouble> fuzzySet2 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.8D),
				new FuzzyElementDouble(3D, 0.8D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySet<FuzzyElementDouble> unitedSet = fuzzySet1.union(fuzzySet2);
		assertEquals(5, unitedSet.size());
	}
	
	@Test void testUnitonSameSets() {
		FuzzySet<FuzzyElementDouble> fuzzySet1 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySet<FuzzyElementDouble> fuzzySet2 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySet<FuzzyElementDouble> unitedSet = fuzzySet1.union(fuzzySet2);
		assertEquals(fuzzySet1, unitedSet);
		assertEquals(fuzzySet2, unitedSet);
	}
	
	@Test void testUnitonNoLineIntersect() {
		FuzzySet<FuzzyElementDouble> fuzzySet1 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySet<FuzzyElementDouble> fuzzySet2 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.4D),
				new FuzzyElementDouble(2D, 0.6D),
				new FuzzyElementDouble(3D, 0.8D),
				new FuzzyElementDouble(5D, 0.4D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySet<FuzzyElementDouble> unitedSet = fuzzySet1.union(fuzzySet2);
		assertEquals(fuzzySet1, unitedSet);
	}
	
	@Test void testUnitonWithEmptySet() {
		FuzzySet<FuzzyElementDouble> fuzzySet1 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySet<FuzzyElementDouble> fuzzySet2 = new FuzzySetRealsLinearContinuous(new HashSet<>());
		
		FuzzySet<FuzzyElementDouble> unitedSet = fuzzySet1.union(fuzzySet2);
		assertEquals(fuzzySet1.size(), unitedSet.size());
	}
	
	
	@Test
	void testIntersection() {
		FuzzySet<FuzzyElementDouble> fuzzySet1 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySet<FuzzyElementDouble> fuzzySet2 = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.4D),
				new FuzzyElementDouble(2D, 0.6D),
				new FuzzyElementDouble(3D, 0.8D),
				new FuzzyElementDouble(5D, 0.4D)
				).stream().collect(Collectors.toSet()));
		
		FuzzySet<FuzzyElementDouble> unitedSet = fuzzySet1.intersection(fuzzySet2);
		assertEquals(fuzzySet2, unitedSet);
	}
	
	@Test
	void testComplement() {
		FuzzySet<FuzzyElementDouble> fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 0.7D),
				new FuzzyElementDouble(3D, 0.9D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		FuzzySet<FuzzyElementDouble> fuzzySetComplement = fuzzySet.complement();
		assertEquals(FuzzyValue.of(0.3), fuzzySetComplement.getElements().get(1).getMembershipDegree());
		
	}
	
	
	@Test
	void testCreateFuzzyNumbers() {
		FuzzySetRealsLinearContinuous fuzzySet = FuzzySetRealsLinearContinuous.ofSymmetricTriangularFuzzyNumber(5D, 0.5D);
		assertEquals(FuzzySetType.FUZZY_NUMBER_TRIANGULAR, fuzzySet.getFuzzySetType());
		fuzzySet = FuzzySetRealsLinearContinuous.ofRegularTriangularFuzzyNumber(4D, 4.5D, 7D);
		assertEquals(FuzzySetType.FUZZY_NUMBER_TRIANGULAR, fuzzySet.getFuzzySetType());
		fuzzySet = FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(10D, 12D, 4D);
		assertEquals(FuzzySetType.FUZZY_NUMBER_TRAPEZOIDAL, fuzzySet.getFuzzySetType());
		fuzzySet = FuzzySetRealsLinearContinuous.ofRegularTrapezoidalFuzzyNumber(15D, 15.3D, 16D, 20D);
		assertEquals(FuzzySetType.FUZZY_NUMBER_TRAPEZOIDAL, fuzzySet.getFuzzySetType());
		
		fuzzySet = new FuzzySetRealsLinearContinuous(List.of(
				new FuzzyElementDouble(1D, 0.5D),
				new FuzzyElementDouble(2D, 1D),
				new FuzzyElementDouble(5D, 0.5D)
				).stream().collect(Collectors.toSet()));
		assertEquals(FuzzySetType.GENERAL, fuzzySet.getFuzzySetType());
	}


}
