package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.setbuilder.Superiority.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.mhw.model.EquipmentSet;

public class SearchResult {

	private static final Logger log = LoggerFactory.getLogger(SearchResult.class);

	private int candidateCount;
	private int permutationCount;
	private List<EquipmentSet> solutions = new ArrayList<>();
	private int filteredCandidateCount;
	private int filteredSetCount;

	public void setCandidateList(CandidateList candidateList) {
		setCandidateCount(candidateList.size());
		setPermutationCount(candidateList.getPermutationCount());
		setFilteredCandidateCount(candidateList.getFilteredCandidateCount());
	}

	public void addSolution(EquipmentSet newSolution) {
		Iterator<EquipmentSet> iterator = solutions.iterator();
		while (iterator.hasNext()) {
			EquipmentSet solution = iterator.next();
			Superiority sup = Superiority.compare(newSolution, solution);
			if (sup == WORSE) {
				// New solution is worse than an existing solution, so it should not be used
				log.debug("Will not add {}", newSolution);
				setFilteredSetCount(getFilteredSetCount() + 1);
				return;
			} else if (sup == BETTER) {
				// Potential candidate is better than an existing candidate which should be removed
				log.debug("  Removing {}", solution);
				setFilteredSetCount(getFilteredSetCount() + 1);
				iterator.remove();
			}
		}
		// The new solution is not worse than any existing one, so it should be added
		log.debug("Adding {}", newSolution);
		solutions.add(newSolution);
	}

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

	public int getFilteredCandidateCount() {
		return filteredCandidateCount;
	}

	public void setFilteredCandidateCount(int filteredCandidateCount) {
		this.filteredCandidateCount = filteredCandidateCount;
	}

	public int getFilteredSetCount() {
		return filteredSetCount;
	}

	public void setFilteredSetCount(int filteredSetCount) {
		this.filteredSetCount = filteredSetCount;
	}
}
