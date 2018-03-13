package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.EquipmentType.*;
import static com.bromleyoil.mhw.setbuilder.Superiority.*;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bromleyoil.mhw.comparator.Comparators;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SetBuilder {

	private static Logger log = LoggerFactory.getLogger(SetBuilder.class);

	@Autowired
	private CandidateList candidateList;

	private SkillSet requiredSkillSet = new SkillSet();
	private SlotSet requiredSlotSet = new SlotSet();
	private Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);
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
		doSearch();

		log.info("Found {} solutions", result.getSolutions().size());
		result.getSolutions().sort(Comparators.adapted(EquipmentSet::getSkillSet,
				Comparators.composite(Comparators.skillwise(requiredSkillSet.getSkills()), Comparators.ALL_SKILLS)));

		log.info("Sort complete");
		return result;
	}

	protected void doSearch() {
		for (int i = 0; i < candidateList.size(HEAD); i++) {
			for (int j = 0; j < candidateList.size(BODY); j++) {
				for (int k = 0; k < candidateList.size(HANDS); k++) {
					for (int l = 0; l < candidateList.size(WAIST); l++) {
						for (int m = 0; m < candidateList.size(LEGS); m++) {
							for (int n = 0; n < candidateList.size(CHARM); n++) {
								checkSolution(i, j, k, l, m, n);
								if (result.getSolutions().size() >= 100) {
									log.info("Aborting search with {} solutions.", result.getSolutions().size());
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	protected void checkSolution(int i, int j, int k, int l, int m, int n) {
		EquipmentSet set = new EquipmentSet();
		set.add(candidateList.getCandidates(HEAD).get(i));
		set.add(candidateList.getCandidates(BODY).get(j));
		set.add(candidateList.getCandidates(HANDS).get(k));
		set.add(candidateList.getCandidates(WAIST).get(l));
		set.add(candidateList.getCandidates(LEGS).get(m));
		set.add(candidateList.getCandidates(CHARM).get(n));

		// Compute missing skills and decorate if necessary
		set.decorate(requiredSkillSet, decorationCounts);

		// Check if the potential solution meets the required skills and slots
		if (Superiority.equalOrBetter(set.getSkillSet(), requiredSkillSet)
				&& Superiority.equalOrBetter(set.getSlotSet(), requiredSlotSet)) {
			addSolution(result.getSolutions(), set);
		}
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

	public Map<Skill, Integer> getDecorationCounts() {
		return decorationCounts;
	}

	public void setDecorationCounts(Map<Skill, Integer> decorationCounts) {
		this.decorationCounts = decorationCounts;
	}
}
