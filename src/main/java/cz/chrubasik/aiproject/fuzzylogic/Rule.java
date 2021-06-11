package cz.chrubasik.aiproject.fuzzylogic;

import java.util.HashSet;
import java.util.Set;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class Rule {
	
	private Set<Statement> antecedents;
	private OperatorType operatorType;
	private Statement consequent;
	
	
	public enum OperatorType {
		OR,
		AND
	}

	@NoArgsConstructor
	public static class RuleBuilder {
		
		private Set<Statement> antecedents;
		private OperatorType operatorType;
		private Statement consequent;
		

		public Rule build() {
			return new Rule(this.antecedents, this.operatorType, this.consequent);
		}
		
		public RuleBuilder antecedent(String statement) {
			if (this.antecedents == null) {
				this.antecedents = new HashSet<>();
			}
			this.antecedents.add(new Statement(statement));
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
