package com.bromleyoil.mhw.setbuilder;

import java.util.ArrayList;
import java.util.List;

import com.bromleyoil.mhw.model.EquipmentSet;

public class SearchResult {

	private int candidateCount;
	private int permutationCount;
	private List<EquipmentSet> solutions = new ArrayList<>();

	public int getCandidateCount() {
		return candidateCount;
	}

	public void setCandidateCount(int candidateCount) {
		this.candidateCount = candidateCount;
	}

	public int getPermutationCount() {
		return permutationCount;
	}

	public void setPermutationCount(int permutationCount) {
		this.permutationCount = permutationCount;
	}

	public List<EquipmentSet> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<EquipmentSet> solutions) {
		this.solutions = solutions;
	}
}
