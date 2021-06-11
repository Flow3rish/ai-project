package cz.chrubasik.aiproject.fuzzysets.relations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cz.chrubasik.aiproject.fuzzysets.FuzzyValue;

class FuzzyRelationTest {
	
	private Random rand;
	List<List<FuzzyValue>> relationMatrix;
	
	
	@BeforeEach
	void setUp() {
		rand = new Random(2345234L);
		int N = 30;
		relationMatrix = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			relationMatrix.add(new ArrayList<>());
			for (int j = 0; j < N; j++) {
				relationMatrix.get(i).add(FuzzyValue.FV_0);
			}
		}
	}

	
	@Test
	void testReflexive() {
		for (int i = 0; i < relationMatrix.size(); i++) {
			relationMatrix.get(i).set(i, FuzzyValue.FV_1);
		}
		FuzzyRelation fuzzyRelation = new FuzzyRelation(relationMatrix);
		assertTrue(fuzzyRelation.isReflexive());
		
	}
	
	@Test
	void testSymmetric() {
		for (int i = 0; i < relationMatrix.size(); i++) {
			for (int j = 0; j < relationMatrix.size(); j++) {
				Double d = rand.nextDouble();
				relationMatrix.get(i).set(j, FuzzyValue.of(d));
				relationMatrix.get(j).set(i, FuzzyValue.of(d));
			}
		}
		
		FuzzyRelation fuzzyRelation = new FuzzyRelation(relationMatrix);
		assertTrue(fuzzyRelation.isSymmetric());
		
	}
	
	
	@Test
	void testTransitive() {
		relationMatrix.get(0).set(2, FuzzyValue.of(0.4D));
		relationMatrix.get(2).set(3, FuzzyValue.of(0.5D));
		relationMatrix.get(0).set(3, FuzzyValue.of(0.5D));
		
		FuzzyRelation fuzzyRelation = new FuzzyRelation(relationMatrix);
		assertTrue(fuzzyRelation.isTransitive());
	}
	
	@Test
	void testEquivalence() {
		for (int i = 0; i < relationMatrix.size(); i++) {
			relationMatrix.get(i).set(i, FuzzyValue.FV_1);
		}
		relationMatrix.get(0).set(2, FuzzyValue.of(0.5D));
		relationMatrix.get(2).set(3, FuzzyValue.of(0.5D));
		relationMatrix.get(0).set(3, FuzzyValue.of(0.7D));
		
		relationMatrix.get(2).set(0, FuzzyValue.of(0.5D));
		relationMatrix.get(3).set(2, FuzzyValue.of(0.5D));
		relationMatrix.get(3).set(0, FuzzyValue.of(0.7D));
		
		FuzzyRelation fuzzyRelation = new FuzzyRelation(relationMatrix);
		assertTrue(fuzzyRelation.isEquivalence());
	}
	

}
