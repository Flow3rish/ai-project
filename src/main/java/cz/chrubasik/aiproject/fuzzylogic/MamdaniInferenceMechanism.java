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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	
	public MamdaniInferenceMechanism addMeasurement(String lingVarName, Double measurement) {
		measurements.put(lingVarName, measurement);
		return this;
	}
	
	public MamdaniInferenceMechanism addRule(Rule rule) {
		this.rules.add(rule);
		return this;
	}
	
	public MamdaniInferenceMechanism addLinguisticVariable(FuzzyLinguisticVariable fuzzyLinguisticVariable) {
		this.fuzzyLinguisticVariables.put(fuzzyLinguisticVariable.getName(), fuzzyLinguisticVariable);
		return this;
	}


	/*
	 * evaluate one rule -> one step in the inference mechanism
	 */
	private FuzzySetRealsLinearContinuous evaluateRule(Rule rule, HashMap<String, Double> measurements) {
		List<FuzzyValue> antecedents = rule.getAntecedents().stream().map(el -> {
			FuzzyLinguisticVariable flv = fuzzyLinguisticVariables.get(el.getSubject());
			Double measurement = measurements.get(el.getSubject());
			if (measurement == null) {
				return FuzzyValue.FV_0;
			} else {
				return flv.evaluateMeasurementOnFuzzySet(measurement, el.getPredicate());
			}
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
		
		HashMap<String, FuzzySetRealsLinearContinuous> inferFuzzSets = new HashMap<>();
		outputSets.keySet().forEach(key -> {
			FuzzySetRealsLinearContinuous fuzzySetTemp = outputSets.get(key).stream().reduce(new FuzzySetRealsLinearContinuous(new HashSet<>()), (a, b) -> (FuzzySetRealsLinearContinuous) a.union(b));
			if (!fuzzySetTemp.hasOnlyZeroMemberships()) {
				inferFuzzSets.put(key, fuzzySetTemp);
			}
		});
		this.inferedFuzzySets = inferFuzzSets;
		return inferFuzzSets;
	}
	
	private HashMap<String, FuzzyElementDouble> defuzzifyCOA() {
		if (inferedFuzzySets == null) {
			throw new RuntimeException("runInference was not executed.");
		}
		defuzzifiedElements = new HashMap<>();
		for (String key : inferedFuzzySets.keySet()) {
			FuzzySetRealsLinearContinuous set = inferedFuzzySets.get(key);
			if (set.hasOnlyZeroMemberships()) {
				break;
			}
			Double u = set.centerOfArea();
			FuzzyElementDouble fuzzyElementDouble = new FuzzyElementDouble(u, set.mu_c(u).getValue());
			if (!fuzzyElementDouble.getMembershipDegree().equals(FuzzyValue.FV_0)) {
				defuzzifiedElements.put(key, new FuzzyElementDouble(fuzzyElementDouble.getElement(), set.mu_c(fuzzyElementDouble.getElement()).getValue()));
			}
		}
		return defuzzifiedElements;
		
	}
	
	public HashMap<String, String> resultsInterpretation() {
		this.defuzzifiedElements = defuzzifyCOA();
		HashMap<String, String> interpretation = new HashMap<>();
		if (defuzzifiedElements.isEmpty()) {
			interpretation.put("nothing", "no results");
			return interpretation;
		}
		for (String key: defuzzifiedElements.keySet()) {
			Set<String> flvValues = fuzzyLinguisticVariables.get(key).getM_x().keySet();
			for (String flvKey : flvValues) {
				List<FuzzyElementDouble> l = fuzzyLinguisticVariables.get(key).getM_x().get(flvKey).getElements();
				if (defuzzifiedElements.get(key).getElement().compareTo(l.get(0).getElement()) >= 0 && defuzzifiedElements.get(key).getElement().compareTo(l.get(l.size() - 1).getElement()) <= 0) {
					// defuzzified element is in this language value
					interpretation.put(key, flvKey);
					break; // multiple puts are not possible for this case
				}
			}
		}
		return interpretation;
	}

	
}
