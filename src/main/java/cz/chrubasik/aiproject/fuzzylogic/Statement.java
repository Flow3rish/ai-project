package cz.chrubasik.aiproject.fuzzylogic;

import lombok.Value;

@Value
public class Statement {
	String subject;
	String predicate;
	
	public Statement(String isFormString) {
		String[] words = isFormString.split("\\s+");
		if (words.length != 3) {
			throw new RuntimeException("Invalid statement form");
		}
		this.subject = words[0];
		this.predicate = words[2];
		
	}
}
