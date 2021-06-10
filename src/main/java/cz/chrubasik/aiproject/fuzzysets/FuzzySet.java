package cz.chrubasik.aiproject.fuzzysets;

import java.util.List;

public interface FuzzySet<E> {
	
	FuzzySet<E> intersection(FuzzySet<E> other);
	FuzzySet<E> union(FuzzySet<E> other);
	FuzzySet<E> complement();
	FuzzySet<E> ceil(FuzzyValue fuzzyValue);
	List<E> getElements();
	int size();

}
