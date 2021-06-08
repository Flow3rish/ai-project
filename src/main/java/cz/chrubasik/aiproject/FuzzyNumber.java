/**
 * 
 */
package cz.chrubasik.aiproject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Value;

/**
 * @author Vit Chrubasik
 *
 */
@Value
public class FuzzyNumber implements FuzzySet<Double>{
	static enum FuzzyNumberType {
		TRAPEZOIDAL,
		TRIANGULAR,
		CERTAIN
	}
	
	Set<FuzzyElementDouble> elements;
	FuzzyNumberType fuzzyNumberType;
	
	public FuzzyNumber(Double l, Double c1, Double c2, Double r) {
		if (!(l <= c1 && c1 <= c2 && c2 <= r)) {
			throw new RuntimeException("Invalid values");
		}
		elements = new HashSet<>();
		elements.addAll(List.of(
				new FuzzyElementDouble(l, 0D),
				new FuzzyElementDouble(c1, 1D),
				new FuzzyElementDouble(c2, 1D),
				new FuzzyElementDouble(r, 0D)
				));
		fuzzyNumberType = FuzzyNumberType.TRAPEZOIDAL;
	}
	
	public FuzzyNumber(Double l, Double c, Double r) {
		if (!(l <= c && c <= r)) {
			throw new RuntimeException("Invalid values");
		}
		elements = new HashSet<>();
		elements.addAll(List.of(
				new FuzzyElementDouble(l, 0D),
				new FuzzyElementDouble(c, 1D),
				new FuzzyElementDouble(r, 0D)
				));
		fuzzyNumberType = FuzzyNumberType.TRIANGULAR;
	}
		
	public FuzzyNumber(Double c) {
		elements = new HashSet<>();
		elements.addAll(List.of(
				new FuzzyElementDouble(c, 1D)
				));
		fuzzyNumberType = FuzzyNumberType.CERTAIN;
	}
	
	public List<FuzzyElementDouble> getElements() {
		return new ArrayList<>(elements).stream().sorted(Comparator.comparing(FuzzyElementDouble::getElement)).collect(Collectors.toList());
	}

	@Override
	public FuzzyValue getMembershipDegreeOfElement(Double x) {
		switch (fuzzyNumberType) {
		case CERTAIN:
			if (x.equals(getElements().get(0).getElement())) {
				return getElements().get(0).getMembershipDegree();
			} else {
				return FuzzyValue.FV_0;
			}
		case TRIANGULAR:
			if (x.compareTo(getElements().get(1).getElement()) < 0) {
				return FuzzyValue.of((x - getElements().get(0).getElement()) / (getElements().get(1).getElement() - getElements().get(0).getElement()));
			}
			if (x.equals(getElements().get(1).getElement())) {
				return FuzzyValue.FV_1;
			}
			if (x.compareTo(getElements().get(1).getElement()) > 0) {
				return FuzzyValue.of((getElements().get(2).getElement() - x) / (getElements().get(2).getElement() - getElements().get(1).getElement()));
			}
			else {
				return FuzzyValue.FV_0;
			}
		case TRAPEZOIDAL:
			if (x.compareTo(getElements().get(1).getElement()) < 0) {
				return FuzzyValue.of((x - getElements().get(0).getElement()) / (getElements().get(1).getElement() - getElements().get(0).getElement()));
			}
			if (getElements().get(1).getElement().compareTo(x) <= 0 && x.compareTo(getElements().get(2).getElement()) <= 0) {
				return FuzzyValue.FV_1;
			}
			if (x.compareTo(getElements().get(2).getElement()) > 0) {
				return FuzzyValue.of((getElements().get(3).getElement() - x) / (getElements().get(3).getElement() - getElements().get(2).getElement()));
			}
			else {
				return FuzzyValue.FV_0;
			}

		default:
			throw new RuntimeException("fuzzyNumberType not set.");
		}
	}
		
	
}
