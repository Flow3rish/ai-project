package cz.chrubasik.aiproject.fuzzylogic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class Rule {
	
	private Set<Statement> antecedent;
	private OperatorType operatorType;
	private Statement consequent;
	
	
	public enum OperatorType {
		OR,
		AND
	}

	@NoArgsConstructor
	public static class RuleBuilder {
		
		private Set<Statement> antecedent;
		private OperatorType operatorType;
		private Statement consequent;
		

		public Rule build() {
			return new Rule(this.antecedent, this.operatorType, this.consequent);
		}
		
		public RuleBuilder antecedent(String ...statements) {
			Set<String> strings = new HashSet<>(Arrays.asList(statements));
			this.antecedent = strings.stream().map(el -> new Statement(el)).collect(Collectors.toSet());
			return this;
			
		}
		
		public RuleBuilder operatorType(OperatorType operatorType) {
			this.operatorType = operatorType;
			return this;
			
		}
		
		public RuleBuilder consequent(String consequent) {
			this.consequent = new Statement(consequent);
			return this;
		}
	}

	
	public static RuleBuilder builder() {
		return new RuleBuilder();
	}
	
	
}
