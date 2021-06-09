package cz.chrubasik.aiproject;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public final class FuzzyValue {


	private Double value;
	static Double NO_VALUE = Double.NEGATIVE_INFINITY;
	static FuzzyValue FV_0 = new FuzzyValue(0D);
	static FuzzyValue FV_1 = new FuzzyValue(1D);
	static FuzzyValue FV_NONE = new FuzzyValue(null);
	
	private FuzzyValue(Double value) {
		if (value == null) {
			this.value = value;
		} else if (value < 0D && value != NO_VALUE) {
			this.value = 0D;
		} else if (value > 1D) {
			this.value = 1D;
		} else {
			this.value = Double.valueOf(Math.round(value * 10000D)/10000D);
		}
	}
	
	public static FuzzyValue of(Double value) {
		return new FuzzyValue(value);
	}
	
	public int compareTo(FuzzyValue other) {
		return this.getValue().compareTo(other.getValue());
	}
	
	public String toString() {
		return "FuzzyValue(" + value.toString() + ")";
	}
}
