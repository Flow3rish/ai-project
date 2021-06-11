package cz.chrubasik.aiproject.fuzzysets.relations;


public interface Relation<T> {
	Boolean isReflexive();
	Boolean isSymmetric();
	Boolean isTransitive();
	default Boolean isEquivalence() {
		return isReflexive() && isSymmetric() && isTransitive();
	}
}
