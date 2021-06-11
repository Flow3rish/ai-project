package cz.chrubasik.aiproject.fuzzylogic;

import java.util.HashMap;
import java.util.Set;

import cz.chrubasik.aiproject.fuzzysets.FuzzyElementDouble;
import cz.chrubasik.aiproject.fuzzysets.FuzzySetRealsLinearContinuous;
import cz.chrubasik.aiproject.fuzzysets.FuzzyValue;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

/*
 * Universe of discourse: real numbers
 * 
 */
@Value
@EqualsAndHashCode
public final class FuzzyLinguisticVariable {
	/*
	 * name of the fuzzy language variable
	 * e.g. tree height
	 */
	String name;
	
	
	Set<String> l_x;
	
	/*
	 * @keys: l_x
	 * @values: f_x
	 */
	HashMap<String, FuzzySetRealsLinearContinuous> m_x;
	
	public FuzzyLinguisticVariable(String name , HashMap<String, FuzzySetRealsLinearContinuous> m_x) {
		this.name = name;
		this.m_x = m_x;
		this.l_x = m_x.keySet();
	}
	
	public HashMap<String, FuzzyElementDouble> membershipsForMeasurement(Double x) {
		HashMap<String, FuzzyElementDouble> hm = new HashMap<>();
		l_x.forEach(el -> {
			hm.put(el, new FuzzyElementDouble(x, m_x.get(el).mu_c(x).getValue()));
		});
		return hm;
	}

	/*
	 * returns membership value on given set of ling. var.
	 */
	public FuzzyValue evaluateMeasurementOnFuzzySet(Double x, String f_x) {
		return m_x.get(f_x).mu_c(x);
	}

	
	
	@NoArgsConstructor
	public static class FuzzyLinguisticVariableBuilder {
		private String name;
		private HashMap<String, FuzzySetRealsLinearContinuous> m_x = new HashMap<>();
		
		public FuzzyLinguisticVariableBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public FuzzyLinguisticVariableBuilder addLinguisticValue(String name, FuzzySetRealsLinearContinuous fuzzyNumber) {
			m_x.put(name, fuzzyNumber);
			return this;
		}
		
		public FuzzyLinguisticVariable build() {
			return new FuzzyLinguisticVariable(this.name, this.m_x);
		}
	}
	
	public static FuzzyLinguisticVariableBuilder builder() {
		return new FuzzyLinguisticVariableBuilder();
	}
	
}
