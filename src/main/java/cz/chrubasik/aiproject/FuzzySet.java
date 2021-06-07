package cz.chrubasik.aiproject;

public interface FuzzySet<E> {
	
	public FuzzyValue getMembershipDegreeOfElement(E x);

}
