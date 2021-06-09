package cz.chrubasik.aiproject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Value;

@Value
public final class FuzzySetRealsLinearContinuous implements FuzzySet<Double> {
	
	Set<FuzzyElementDouble> elements;
	FuzzySetType fuzzySetType;
	
	static enum FuzzySetType {
		ONE_ELEMENT,
		TRAPEZOIDAL,
		TRIANGULAR,
		GENERAL,
		EMPTY
	}
	
	public FuzzySetRealsLinearContinuous(Set<FuzzyElementDouble> elements) {
		this.elements = elements;
		switch (this.elements.size()) {
		case 0:
			this.fuzzySetType = FuzzySetType.EMPTY;
			break;
		case 1:
			this.fuzzySetType = FuzzySetType.ONE_ELEMENT;
			break;
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
	
		return mu_c(x.getElement(), y.getElement(), c, x.getMembershipDegree(), y.getMembershipDegree());
		
	}
	
	private void ceiledAddToSet(Set<FuzzyElementDouble> set, FuzzyElementDouble el, FuzzyValue ceiling) {
		if (el.getMembershipDegree().compareTo(ceiling) <= 0) {
			set.add(el);
		}
	}
	
	
	private FuzzyValue mu_c(Double x, Double y, Double c, FuzzyValue mu_x, FuzzyValue mu_y) {
		if (mu_y.compareTo(mu_x) < 0 || mu_y.compareTo(mu_x) > 0) {
			//Double memDegreeValue = ((y - c) / (y - x)) * (mu_x.getValue() - mu_y.getValue()) + mu_y.getValue(); // equivalent
			Double memDegreeValue = ((c - x) / (y - x)) * (mu_y.getValue() - mu_x.getValue()) + mu_x.getValue();
			return FuzzyValue.of(memDegreeValue);
			
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
	
	
	private Double muCInverse(Double x, Double y, FuzzyValue mu_c, FuzzyValue mu_x, FuzzyValue mu_y) {
		// TODO after check if no duplicates exist in the set, revise this
		if (mu_c.compareTo(mu_x) < 0 && mu_c.compareTo(mu_y) < 0) {
			return null; // ceiling below both values
		}
		if (mu_c.compareTo(mu_x) > 0 && mu_c.compareTo(mu_y) > 0) {
			return null; // ceiling above both values
		}
		if (mu_y.equals(mu_x)) {
			return null; // undefined, horizontal line gives vertical line, which is not a function
		}
		if (mu_c.equals(mu_x)) {
			return x;
		}
		if (mu_c.equals(mu_y)) {
			return y;
		}
		if (mu_y.compareTo(mu_x) < 0 || mu_y.compareTo(mu_x) > 0) {
			return x + ((y - x) / (mu_y.getValue() - mu_x.getValue())) * (mu_y.getValue() - mu_c.getValue());
			
		}
//		if (mu_y.compareTo(mu_x) > 0) {
//			return y - ((y - x) / (mu_x.getValue() - mu_y.getValue())) * (mu_x.getValue() - mu_c.getValue());
//		}
		else {
			throw new RuntimeException("Unhandled case in membership funcion");
		}
		
	}
	
	public FuzzyValue muCOnWholeSet(Double c) {
		List<FuzzyElementDouble> l = getElements();
		FuzzyElementDouble x = null;
		FuzzyElementDouble y = null;
		if(c.compareTo(l.get(0).getElement()) < 0 || c.compareTo(l.get(l.size() - 1).getElement()) > 0) { // outside of defined interval
			return FuzzyValue.FV_0;
		}
		for (int i = 0; i < l.size() - 1; i++) {
			if(c.compareTo(l.get(i).getElement()) >= 0 && c.compareTo(l.get(i + 1).getElement()) <= 0) {
				x = l.get(i);
				y = l.get(i + 1);
				return mu_c(x.getElement(), y.getElement(), c, x.getMembershipDegree(), y.getMembershipDegree());
			}
		}
		throw new RuntimeException("Unhandled case in muWholeset");
	}
	
	public static FuzzyElementDouble _twoLineIntersection(FuzzyElementDouble e1, FuzzyElementDouble e2, FuzzyElementDouble f1, FuzzyElementDouble f2) {
		Double x1, y1, x2, y2, x3, y3, x4, y4;
		x1 = e1.getElement();
		y1 = e1.getMembershipDegree().getValue();
		x2 = e2.getElement();
		y2 = e2.getMembershipDegree().getValue();
		x3 = f1.getElement();
		y3 = f1.getMembershipDegree().getValue();
		x4 = f2.getElement();
		y4 = f2.getMembershipDegree().getValue();
		Double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		Double u = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		if (t.compareTo(0D) >= 0 && t.compareTo(1D) <= 0D && (u.compareTo(0D) >= 0 && u.compareTo(1D) <= 0D)) {
			return new FuzzyElementDouble(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
		}
		return null;
		
	}
	
	public FuzzySetRealsLinearContinuous ceilSet(FuzzyValue ceiling) {
		List<FuzzyElementDouble> l = getElements();
		Set<FuzzyElementDouble> elements = new HashSet<>();
		FuzzyElementDouble e1 = null;
		FuzzyElementDouble e2 = null;
		Double intersect = null;
		l.forEach(el -> ceiledAddToSet(elements, el, ceiling));
		for (int i = 0; i < l.size() - 1; i++) {
			e1 = l.get(i);
			e2 = l.get(i + 1);
			intersect = muCInverse(e1.getElement(), e2.getElement(), ceiling, e1.getMembershipDegree(), e2.getMembershipDegree());
			if (intersect != null) {
				ceiledAddToSet(elements, new FuzzyElementDouble(intersect, ceiling.getValue()), ceiling);
			}
		}
		return new FuzzySetRealsLinearContinuous(elements);
	}
	
	
	private static boolean _setOperationSwitch(int comparingCondition, String operation) {
		switch (operation) {
		case "union":
			return comparingCondition >= 0;
		case "intersection":
			return comparingCondition <= 0;

		default:
			throw new RuntimeException("Invalid operation");
		}
	}
	
	public FuzzySetRealsLinearContinuous union(FuzzySetRealsLinearContinuous other) {
		return setOperaion(other, "union");
	}
	
	public FuzzySetRealsLinearContinuous intersection(FuzzySetRealsLinearContinuous other) {
		return setOperaion(other, "intersection");
	}
	
	
	
	private FuzzySetRealsLinearContinuous setOperaion(FuzzySetRealsLinearContinuous other, String operation) {
		List<FuzzyElementDouble> lThis = getElements();
		List<FuzzyElementDouble> lOther = other.getElements();
		Set<FuzzyElementDouble> elements = new HashSet<>();
		FuzzyValue muC = null;
		
		// add larger points of the two sets (two for loops)
		for (int i = 0; i < lThis.size(); i++) {
			muC = other.muCOnWholeSet(lThis.get(i).getElement());
			if (_setOperationSwitch(lThis.get(i).getMembershipDegree().compareTo(muC), operation)) { // if the element is above or equal to the membership for second
				elements.add(new FuzzyElementDouble(lThis.get(i).getElement(), lThis.get(i).getMembershipDegree().getValue()));
			}
		}
		for (int i = 0; i < lOther.size(); i++) {
			muC = this.muCOnWholeSet(lOther.get(i).getElement());
			if (_setOperationSwitch(lOther.get(i).getMembershipDegree().compareTo(muC), operation)) {
				elements.add(new FuzzyElementDouble(lOther.get(i).getElement(), lOther.get(i).getMembershipDegree().getValue()));
			}
		}
		
		// evaluate intersects
		for (int i = 0; i < lThis.size() - 1; i++) {
			for (int j = 0; j < lOther.size() - 1; j++) {
				FuzzyElementDouble e1 = lThis.get(i);
				FuzzyElementDouble e2 = lThis.get(i + 1);
				FuzzyElementDouble e3 = lOther.get(j);
				FuzzyElementDouble e4 = lOther.get(j + 1);
				
				FuzzyElementDouble intersect = FuzzySetRealsLinearContinuous._twoLineIntersection(e1, e2, e3, e4);
				if (intersect != null) {
					elements.add(intersect);
				}
			}
		}
		return new FuzzySetRealsLinearContinuous(elements);
		
		
	}
	
	public FuzzySetRealsLinearContinuous complement() {
		return new FuzzySetRealsLinearContinuous(this.getElements().stream().map(el -> new FuzzyElementDouble(el.getElement(), 1 - el.getMembershipDegree().getValue())).collect(Collectors.toSet()));
	}
	
	
	public int size() {
		return elements.size();
	}
	
	public String toString() {
		return "FuzzySet(\n" + getElements().stream().map(el -> el.toString() + "\n").collect(Collectors.toList()) + ")";
	}
	

}
