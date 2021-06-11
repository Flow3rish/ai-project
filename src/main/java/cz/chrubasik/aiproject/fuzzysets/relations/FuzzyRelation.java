package cz.chrubasik.aiproject.fuzzysets.relations;

import java.util.List;

import cz.chrubasik.aiproject.fuzzysets.FuzzyValue;

public class FuzzyRelation implements Relation<FuzzyValue> {

	private List<List<FuzzyValue>> relationMatrix;

	public FuzzyRelation(List<List<FuzzyValue>> relationMatrix) {
		if (!(relationMatrix.size() == relationMatrix.get(0).size())) {
			throw new RuntimeException("Invalid matrix dimensions");
		}
		this.relationMatrix = relationMatrix;
	}
	@Override
	public Boolean isReflexive() {
		for (int i = 0; i < relationMatrix.size(); i++) {
			if (!relationMatrix.get(i).get(i).equals(FuzzyValue.FV_1)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Boolean isSymmetric() {
		for (int i = 0; i < relationMatrix.size(); i++) {
			for (int j = 0; j < relationMatrix.size(); j++) {
				if (!(this.relationMatrix.get(i).get(j).equals(relationMatrix.get(j).get(i)))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Boolean isTransitive() {
		for (int i = 0; i < relationMatrix.size(); i++) {
			for (int j = 0; j < relationMatrix.size(); j++) {
				if (!relationMatrix.get(i).get(j).equals(0D)) {
					for (int k = 0; k < relationMatrix.size(); k++) {
						Double lambda_1 = relationMatrix.get(i).get(j).getValue();
						Double lambda_2 = relationMatrix.get(j).get(k).getValue();
						Double lambda = relationMatrix.get(i).get(k).getValue();
						if (!(lambda.compareTo(Math.min(lambda_1, lambda_2)) >= 0)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

}
