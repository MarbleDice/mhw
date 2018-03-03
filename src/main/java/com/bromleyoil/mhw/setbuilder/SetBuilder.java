package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.EquipmentType.*;
import static com.bromleyoil.mhw.setbuilder.Superiority.*;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SetBuilder {

	private static Logger log = LoggerFactory.getLogger(SetBuilder.class);

	@Autowired
	private CandidateList candidateList;

	private SkillSet requiredSkillSet = new SkillSet();;
	private SlotSet requiredSlotSet = new SlotSet();
	private SearchResult result;

	public SetBuilder() {
	}

	public SetBuilder(CandidateList candidateList) {
		this.candidateList = candidateList;
	}

	public SearchResult search() {
		result = new SearchResult();

		candidateList.setRequiredSkillSet(requiredSkillSet);
		result.setCandidateCount(candidateList.size());
		result.setPermutationCount(candidateList.getPermutationCount());
		result.setFilteredCandidateCount(candidateList.getFilteredCandidateCount());

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
								// Check if the potential solution meets the required skills and slots
								Superiority sup = Superiority.combine(
										Superiority.compare(set.getSkillSet(), requiredSkillSet),
										Superiority.compare(set.getSlotSet(), requiredSlotSet));
								if (sup == BETTER || sup == EQUAL) {
									addSolution(result.getSolutions(), set);
								}
								if (result.getSolutions().size() >= 100) {
									log.info("Aborting search with {} solutions.", result.getSolutions().size());
									return result;
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
				result.setFilteredSetCount(result.getFilteredSetCount() + 1);
				return;
			} else if (sup == BETTER) {
				// Potential candidate is better than an existing candidate which should be removed
				log.debug("  Removing {}", solution);
				result.setFilteredSetCount(result.getFilteredSetCount() + 1);
				iterator.remove();
			}
		}
		// The new solution is not worse than any existing one, so it should be added
		log.debug("Adding {}", newSolution);
		solutions.add(newSolution);
	}

	public SkillSet getRequiredSkillSet() {
		return requiredSkillSet;
	}

	public void setRequiredSkillSet(SkillSet requiredSkillSet) {
		this.requiredSkillSet = requiredSkillSet;
	}

	public SlotSet getRequiredSlotSet() {
		return requiredSlotSet;
	}

	public void setRequiredSlotSet(SlotSet requiredSlotSet) {
		this.requiredSlotSet = requiredSlotSet;
	}
}
