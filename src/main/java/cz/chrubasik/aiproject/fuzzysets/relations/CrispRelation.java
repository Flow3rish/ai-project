package cz.chrubasik.aiproject.fuzzysets.relations;

import java.util.List;

import lombok.Value;

@Value
public class CrispRelation implements Relation<Boolean> {

	private List<List<Boolean>> relationMatrix;

	public CrispRelation(List<List<Boolean>> relationMatrix) {
		if (!(relationMatrix.size() == relationMatrix.get(0).size())) {
			throw new RuntimeException("Invalid matrix dimensions");
		}
		this.relationMatrix = relationMatrix;
	}

	@Override
	public Boolean isReflexive() {
		for (int i = 0; i < this.relationMatrix.size(); i++) {
			if (!this.relationMatrix.get(i).get(i)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Boolean isSymmetric() {
		for (int i = 0; i < this.relationMatrix.size(); i++) {
			for (int j = 0; j < this.relationMatrix.size(); j++) {
				if (!(this.relationMatrix.get(i).get(j) == this.relationMatrix.get(j).get(i))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Boolean isTransitive() {
		for (int i = 0; i < this.relationMatrix.size(); i++) {
			for (int j = 0; j < this.relationMatrix.size(); j++) {
				if (this.relationMatrix.get(i).get(j)) {
					for (int k = 0; k < this.relationMatrix.size(); k++) {
						if (this.relationMatrix.get(j).get(k) && !this.relationMatrix.get(i).get(k)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

}
