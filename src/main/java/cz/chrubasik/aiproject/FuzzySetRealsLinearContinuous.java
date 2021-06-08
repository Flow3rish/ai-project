package cz.chrubasik.aiproject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Value;

@Value
public class FuzzySetRealsLinearContinuous implements FuzzySet<Double> {
	
	Set<FuzzyElementDouble> elements;
	FuzzySetType fuzzySetType;
	
	static enum FuzzySetType {
		TRAPEZOIDAL,
		TRIANGULAR,
		GENERAL
	}
	
	public FuzzySetRealsLinearContinuous(Set<FuzzyElementDouble> elements) {
		this.elements = elements;
		switch (this.elements.size()) {
		case 1:
			throw new RuntimeException("No use case for a fuzzy set with one element");
		case 3:
			this.fuzzySetType = FuzzySetType.TRIANGULAR;
			break;
		case 4:
			this.fuzzySetType = FuzzySetType.TRAPEZOIDAL;
			break;
		default:
			this.fuzzySetType = FuzzySetType.GENERAL;
			break;
		}
	}
	
	public List<FuzzyElementDouble> getElements() {
		return new ArrayList<>(elements).stream().sorted(Comparator.comparing(FuzzyElementDouble::getElement)).collect(Collectors.toList());
	}
	

	@Override
	public FuzzyValue getMembershipDegreeOfElement(Double c) {
		List<FuzzyElementDouble> l = getElements();
		FuzzyElementDouble x = null;
		FuzzyElementDouble y = null;
		if (l.get(0).getElement().compareTo(c) > 0 || l.get(l.size() - 1).getElement().compareTo(c) < 0) {
			// not in defined intervals
			return FuzzyValue.FV_0;
		}
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getElement().compareTo(c) <= 0 && l.get(i + 1).getElement().compareTo(c) >= 0) {
				x = l.get(i);
				y = l.get(i + 1);
				break;
			}
		}
		
		// check if not on "sharp turn"
		if (c.equals(x.getElement())) {
			return x.getMembershipDegree();
		}
		if (c.equals(y.getElement())) {
			return y.getMembershipDegree();
		}
		
		if (y.getMembershipDegree().equals(x.getMembershipDegree())) {
			return y.getMembershipDegree(); // x works too, horizontal line
		}
		
		if (y.getMembershipDegree().compareTo(x.getMembershipDegree()) < 0) {
			Double memDegreeValue = ((c - x.getElement()) / (y.getElement() - x.getElement())) * (y.getMembershipDegree().getValue() - x.getMembershipDegree().getValue()) + x.getMembershipDegree().getValue();
			return FuzzyValue.of(memDegreeValue);
		}
		if (y.getMembershipDegree().compareTo(x.getMembershipDegree()) > 0) {
			Double memDegreeValue = ((y.getElement() - c) / (y.getElement() - x.getElement())) * (x.getMembershipDegree().getValue() - y.getMembershipDegree().getValue()) + y.getMembershipDegree().getValue();
			return FuzzyValue.of(memDegreeValue);
			
		}
		else {
			throw new RuntimeException("Unhandled case in membership funcion");
		}
		
	}
	
	public FuzzySetRealsLinearContinuous ceiling(Double ceiling) {
		Set<FuzzyElementDouble> newElements = new HashSet<>();
		List<FuzzyElementDouble> l = getElements();
		for (int i = 0; i < l.size(); i++) {
			
		}
	}
	
	private FuzzyValue mu_c(Double x, Double y, Double c, FuzzyValue mu_x, FuzzyValue mu_y) {
		if (mu_y.compareTo(mu_x) < 0) {
			Double memDegreeValue = ((c - x) / (y - x)) * (mu_y.getValue() - mu_x.getValue()) + mu_x.getValue();
			return FuzzyValue.of(memDegreeValue);
			
		}
		if (mu_y.compareTo(mu_x) > 0) {
			
		}
		if (c.equals(x)) {
			return mu_x;
		}
		if (c.equals(y)) {
			return mu_y;
		}
		if (mu_y.equals(mu_x)) {
			return mu_y; // x works too, horizontal line	
		}
		else {
			throw new RuntimeException("Unhandled case in membership funcion");
		}
	}
	

}
