package cz.chrubasik.aiproject.fuzzylogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cz.chrubasik.aiproject.fuzzylogic.Rule.OperatorType;
import cz.chrubasik.aiproject.fuzzysets.FuzzyElementDouble;
import cz.chrubasik.aiproject.fuzzysets.FuzzySetRealsLinearContinuous;
import cz.chrubasik.aiproject.fuzzysets.FuzzyValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MamdaniInferenceMechanism {
	private HashMap<String, FuzzyLinguisticVariable> fuzzyLinguisticVariables = new HashMap<>();
	private Set<Rule> rules = new HashSet<>();
	private HashMap<String, Double> measurements = new HashMap<>();
	private HashMap<String, FuzzySetRealsLinearContinuous> inferedFuzzySets = null;
	private HashMap<String, FuzzyElementDouble> defuzzifiedElements; // fuzzy value can be omitted
	
	public void addMeasurement(String lingVarName, Double measurement) {
		measurements.put(lingVarName, measurement);
	}
	
	public void addRule(Rule rule) {
		this.rules.add(rule);
	}
	
	public void addLinguisticVariable(FuzzyLinguisticVariable fuzzyLinguisticVariable) {
		this.fuzzyLinguisticVariables.put(fuzzyLinguisticVariable.getName(), fuzzyLinguisticVariable);
	}


	/*
	 * evaluate one rule -> one step in the inference mechanism
	 */
	private FuzzySetRealsLinearContinuous evaluateRule(Rule rule, HashMap<String, Double> measurements) {
		List<FuzzyValue> antecedents = rule.getAntecedent().stream().map(el -> {
			FuzzyLinguisticVariable flv = fuzzyLinguisticVariables.get(el.getSubject());
			return flv.evaluateMeasurementOnFuzzySet(measurements.get(el.getSubject()), el.getPredicate());
		}
			).collect(Collectors.toList());
		FuzzyValue antecedentValue = rule.getOperatorType().equals(OperatorType.OR) ? 
				antecedents.stream().reduce(FuzzyValue.FV_0, (a, b) -> FuzzyValue.of(Math.max(a.getValue(), b.getValue()))) 
				: antecedents.stream().reduce(FuzzyValue.FV_1, (a, b) -> FuzzyValue.of(Math.min(a.getValue(), b.getValue())));
		return fuzzyLinguisticVariables.get(rule.getConsequent().getSubject()).getM_x().get(rule.getConsequent().getPredicate()).ceil(antecedentValue);
	}
	
	public HashMap<String, FuzzySetRealsLinearContinuous> runInference() {
		if (fuzzyLinguisticVariables.isEmpty() || rules.isEmpty() || measurements.isEmpty()) {
			throw new RuntimeException("The mechanism is not set up");
		}
		HashMap<String, Set<FuzzySetRealsLinearContinuous>> outputSets = new HashMap<>();
		this.rules.forEach(el -> {
			
			el.getConsequent().getSubject();
			
			if (outputSets.containsKey(el.getConsequent().getSubject())) { // create new key or update value for existing
				outputSets.get(el.getConsequent().getSubject()).add(evaluateRule(el, measurements));
			} else {
				Set<FuzzySetRealsLinearContinuous> newEls = new HashSet<>();
				newEls.add(evaluateRule(el, measurements));
				outputSets.put(el.getConsequent().getSubject(), newEls);
			}
		});
		
		HashMap<String, FuzzySetRealsLinearContinuous> inferedFuzzySets = new HashMap<>();
		outputSets.keySet().forEach(key -> {
			FuzzySetRealsLinearContinuous fuzzySetTemp = outputSets.get(key).stream().reduce(new FuzzySetRealsLinearContinuous(new HashSet<>()), (a, b) -> (FuzzySetRealsLinearContinuous) a.union(b));
			inferedFuzzySets.put(key, fuzzySetTemp);
		});
		this.inferedFuzzySets = inferedFuzzySets;
		return inferedFuzzySets;
	}
	
	public HashMap<String, FuzzyElementDouble> defuzzifyCOA() {
		if (inferedFuzzySets == null) {
			throw new RuntimeException("runInference was not executed.");
		}
		defuzzifiedElements = new HashMap<>();
		Double u_numerator = 0D;
		Double u_denominator = 0D;
		for (String key : inferedFuzzySets.keySet()) {
			FuzzySetRealsLinearContinuous set = inferedFuzzySets.get(key);
			List<FuzzyElementDouble> l = set.getElements();
			for (FuzzyElementDouble el : l) {
				u_numerator += el.getElement() * el.getMembershipDegree().getValue();
				u_denominator += el.getMembershipDegree().getValue();
			}
			FuzzyElementDouble fuzzyElementDouble = new FuzzyElementDouble(u_numerator/u_denominator, 0D);
			defuzzifiedElements.put(key, new FuzzyElementDouble(u_numerator/u_denominator, set.mu_c(fuzzyElementDouble.getElement()).getValue())); // FIXME someday, move rounding
		}
		return defuzzifiedElements;
		
	}
	
}
