package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.EquipmentType.*;
import static com.bromleyoil.mhw.setbuilder.Superiority.*;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.SkillSet;

@Configurable
public class SetBuilder {

	private static Logger log = LoggerFactory.getLogger(SetBuilder.class);

	public SearchResult search(EquipmentList equipmentList, SkillSet requiredSkillSet) {
		SearchResult result = new SearchResult();

		CandidateList candidateList = new CandidateList(equipmentList, requiredSkillSet);
		result.setCandidateCount(candidateList.size());
		result.setPermutationCount(candidateList.getPermutationCount());

		log.info("Searching {} possibilities", candidateList.getPermutationCount());
		for (int i = 0; i < candidateList.size(HEAD); i++) {
			for (int j = 0; j < candidateList.size(BODY); j++) {
				for (int k = 0; k < candidateList.size(HANDS); k++) {
					for (int l = 0; l < candidateList.size(WAIST); l++) {
						for (int m = 0; m < candidateList.size(LEGS); m++) {
							for (int n = 0; n < candidateList.size(CHARM); n++) {
								EquipmentSet set = new EquipmentSet();
								set.add(candidateList.getCandidates(HEAD).get(i));
								set.add(candidateList.getCandidates(BODY).get(j));
								set.add(candidateList.getCandidates(HANDS).get(k));
								set.add(candidateList.getCandidates(WAIST).get(l));
								set.add(candidateList.getCandidates(LEGS).get(m));
								set.add(candidateList.getCandidates(CHARM).get(n));
								// Check if the potential solution meets the required skills
								Superiority sup = Superiority.compare(set.getSkillSet(), requiredSkillSet);
								if (sup == BETTER || sup == EQUAL) {
									addSolution(result.getSolutions(), set);
								}
							}
						}
					}
				}
			}
		}
		log.info("Found {} solutions", result.getSolutions().size());
		return result;
	}

	protected void addSolution(List<EquipmentSet> solutions, EquipmentSet newSolution) {
		Iterator<EquipmentSet> iterator = solutions.iterator();
		while (iterator.hasNext()) {
			EquipmentSet solution = iterator.next();
			Superiority sup = Superiority.compare(newSolution, solution);
			if (sup == WORSE) {
				// New solution is worse than an existing solution, so it should not be used
				log.debug("Will not add {}", newSolution);
				return;
			} else if (sup == BETTER) {
				// Potential candidate is better than an existing candidate which should be removed
				log.debug("  Removing {}", solution);
				iterator.remove();
			}
		}
		// The new solution is not worse than any existing one, so it should be added
		log.debug("Adding {}", newSolution);
		solutions.add(newSolution);
	}
}
