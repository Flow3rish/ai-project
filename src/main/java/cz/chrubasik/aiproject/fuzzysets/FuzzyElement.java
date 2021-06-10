package cz.chrubasik.aiproject.fuzzysets;

public interface FuzzyElement<T> {
	T getElement();
	FuzzyValue getMembershipDegree();
}
