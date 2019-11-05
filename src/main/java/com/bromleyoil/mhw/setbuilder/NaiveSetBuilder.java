package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.EquipmentType.*;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bromleyoil.mhw.comparator.SkillwiseComparator;
import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NaiveSetBuilder implements SetBuilder {

	private static Logger log = LoggerFactory.getLogger(NaiveSetBuilder.class);

	@Autowired
	private CandidateList candidateList;

	private SkillSet requiredSkillSet = new SkillSet();
	private SlotSet requiredSlotSet = new SlotSet();
	private SlotSet weaponSlotSet = new SlotSet();
	private Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);
	private SearchResult result;

	public NaiveSetBuilder() {
	}

	public NaiveSetBuilder(CandidateList candidateList) {
		this.candidateList = candidateList;
	}

	@Override
	public SearchResult search(SetBuilderForm form) {
		requiredSkillSet = form.getRequiredSkillSet();
		requiredSlotSet = form.getRequiredSlotSet();
		weaponSlotSet = form.getWeaponSlotSet();
		decorationCounts = form.getDecorationCounts();

		candidateList.buildCandidates(form);

		result = new SearchResult();
		result.setCandidateList(candidateList);

		log.info("Searching {} possibilities", candidateList.getPermutationCount());
		doSearch();

		log.info("Found {} solutions", result.getSolutions().size());
		result.getSolutions().sort(Comparator.comparing(EquipmentSet::getSkillSet, SkillwiseComparator.of(requiredSkillSet))
						.thenComparing(Comparator.comparing(EquipmentSet::getSkillSet, SkillwiseComparator.ALL_SKILLS)));

		log.info("Sort complete");
		return result;
	}

	protected void doSearch() {
		for (int i = 0; i < candidateList.size(HEAD); i++) {
			for (int j = 0; j < candidateList.size(CHEST); j++) {
				for (int k = 0; k < candidateList.size(ARM); k++) {
					for (int l = 0; l < candidateList.size(WAIST); l++) {
						for (int m = 0; m < candidateList.size(LEG); m++) {
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
		set.setWeaponSlotSet(weaponSlotSet);
		set.add(candidateList.getCandidates(HEAD).get(i));
		set.add(candidateList.getCandidates(CHEST).get(j));
		set.add(candidateList.getCandidates(ARM).get(k));
		set.add(candidateList.getCandidates(WAIST).get(l));
		set.add(candidateList.getCandidates(LEG).get(m));
		set.add(candidateList.getCandidates(CHARM).get(n));

		// Compute missing skills and decorate if necessary
		set.decorate(requiredSkillSet, decorationCounts);

		// Check if the potential solution meets the required skills and slots
		if (Superiority.equalOrBetter(set.getSkillSet(), requiredSkillSet)
				&& Superiority.equalOrBetter(set.getSlotSet(), requiredSlotSet)) {
			result.addSolution(set);
		}
	}
}
