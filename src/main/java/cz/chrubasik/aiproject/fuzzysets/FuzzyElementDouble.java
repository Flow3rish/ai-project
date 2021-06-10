/**
 * 
 */
package cz.chrubasik.aiproject.fuzzysets;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Vit Chrubasik
 *
 */
@Value
@EqualsAndHashCode
public final class FuzzyElementDouble implements FuzzyElement<Double> {
	
	Double element;
	FuzzyValue membershipDegree;
	
	public FuzzyElementDouble(Double element, Double membershipDegreeValue) {
		this.element = Double.valueOf(Math.round(element * 10000D)/10000D);
		this.membershipDegree = FuzzyValue.of(membershipDegreeValue);
	}

	@Override
	public Double getElement() {
		return element;
	}

	@Override
	public FuzzyValue getMembershipDegree() {
		return membershipDegree;
	}
	
	public int compareTo(FuzzyElementDouble other) {
		return element.compareTo(other.getElement());
	}

}
