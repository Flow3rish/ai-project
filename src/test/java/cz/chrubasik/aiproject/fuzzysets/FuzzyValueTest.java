/**
 * 
 */
package cz.chrubasik.aiproject.fuzzysets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Vit Chrubasik
 *
 */
class FuzzyValueTest {
	

	@Test
	void testCreation() {
		FuzzyValue fuzzyValue = FuzzyValue.of(0.5D);
		assertEquals(0.5D, fuzzyValue.getValue());
		fuzzyValue = FuzzyValue.of(-0.4D);
		assertEquals(0D, fuzzyValue.getValue());
		fuzzyValue = FuzzyValue.of(205D);
		assertEquals(1D, fuzzyValue.getValue());
		fuzzyValue = FuzzyValue.FV_1;
		assertEquals(1D, fuzzyValue.getValue());
		fuzzyValue = FuzzyValue.FV_0;
		assertEquals(0D, fuzzyValue.getValue());
		
	}

}
