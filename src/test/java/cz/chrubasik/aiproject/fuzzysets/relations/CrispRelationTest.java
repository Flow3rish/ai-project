package cz.chrubasik.aiproject.fuzzysets.relations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrispRelationTest {
	
	
	private Random rand;
	List<List<Boolean>> relationMatrix;
	
	
	@BeforeEach
	void setUp() {
		rand = new Random(2345234L);
		int N = 30;
		relationMatrix = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			relationMatrix.add(new ArrayList<>());
			for (int j = 0; j < N; j++) {
				relationMatrix.get(i).add(false);
			}
		}
	}
	
	
	@Test
	void testReflexive() {
		for (int i = 0; i < relationMatrix.size(); i++) {
			relationMatrix.get(i).set(i, true);
		}
		CrispRelation crispRelation = new CrispRelation(relationMatrix);
		assertTrue(crispRelation.isReflexive());
	}

	
	@Test
	void testSymmetric() {
		for (int i = 0; i < relationMatrix.size(); i++) {
			for (int j = 0; j < relationMatrix.size(); j++) {
				Boolean b = rand.nextBoolean();
				relationMatrix.get(i).set(j, b);
				relationMatrix.get(j).set(i, b);
			}
		}
		CrispRelation crispRelation = new CrispRelation(relationMatrix);
		assertTrue(crispRelation.isSymmetric());
		
	}
	
	@Test
	void testTransitive() {
		relationMatrix.get(0).set(2, true);
		relationMatrix.get(2).set(3, true);
		relationMatrix.get(0).set(3, true);
		
		CrispRelation crispRelation = new CrispRelation(relationMatrix);
		assertTrue(crispRelation.isTransitive());
		
	}
	
	
	@Test
	void testEquivalence() {
		for (int i = 0; i < relationMatrix.size(); i++) {
			relationMatrix.get(i).set(i, true);
		}
		relationMatrix.get(0).set(2, true);
		relationMatrix.get(2).set(3, true);
		relationMatrix.get(0).set(3, true);
		
		relationMatrix.get(2).set(0, true);
		relationMatrix.get(3).set(2, true);
		relationMatrix.get(3).set(0, true);
		
		CrispRelation crispRelation = new CrispRelation(relationMatrix);
		assertTrue(crispRelation.isEquivalence());
	}

}
