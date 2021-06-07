package cz.chrubasik.aiproject;

public interface FuzzyElement<T> {
	T getElement();
	FuzzyValue getMembershipDegree();
}
