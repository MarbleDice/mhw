package com.bromleyoil.mhw.setbuilder;

import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Skill;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ParallelSetBuilder implements SetBuilder {

	private static final Logger log = LoggerFactory.getLogger(ParallelSetBuilder.class);
	private static final int MAX_RESULTS = 100;

	@Autowired
	private EquipmentList equipmentList;

	private CandidateList candidateList;

	private int[] lengthByType = new int[EquipmentType.values().length];
	private int[] offsetByType = new int[EquipmentType.values().length];

	int numSkills;
	private byte[] search;
	private byte[] goal;

	private SearchResult searchResult = new SearchResult();

	@Override
	public SearchResult search(SetBuilderForm form) {
		candidateList = new CandidateList(equipmentList);
		candidateList.buildCandidates(form);

		searchResult.setCandidateList(candidateList);

		numSkills = form.getSkills().size();

		// Calculate equipment type block length and offset
		offsetByType = new int[EquipmentType.values().length];
		for (int t = 0; t < EquipmentType.values().length; t++) {
			lengthByType[t] = candidateList.getCandidates(EquipmentType.values()[t]).size();
			offsetByType[t] = t == 0 ? 0 : offsetByType[t - 1] + lengthByType[t - 1] * numSkills;
		}

		// Populate the goal and search array
		goal = new byte[numSkills];
		search = new byte[candidateList.size() * numSkills];
		for (int s = 0; s < numSkills; s++) {
			Skill skill = form.getSkills().get(s);
			goal[s] = (byte) form.getLevels().get(s).intValue();
			for (int t = 0; t < EquipmentType.values().length; t++) {
				List<Equipment> candidates = candidateList.getCandidates(EquipmentType.values()[t]);
				for (int c = 0; c < candidates.size(); c++) {
					search[offsetByType[t] + c * numSkills + s] = (byte) candidates.get(c).getSkillSet()
							.getLevel(skill);
				}
			}
		}

		log.debug("LBT: {}", lengthByType);
		log.debug("OBT: {}", offsetByType);
		log.debug("Goal: {}", goal);

		IntStream.range(0, candidateList.getPermutationCount()).parallel().anyMatch(this::checkSolution);

		return searchResult;
	}

	private boolean checkSolution(int perm) {
		int[] indexByType = calculateIndexByType(perm);

		// TODO needs set bonuses
		for (int s = 0; s < numSkills; s++) {
			byte total = 0;
			for (int t = 0; t < EquipmentType.values().length; t++) {
				total += search[offsetByType[t] + indexByType[t] * numSkills + s];
			}
			if (total < goal[s]) {
				return false;
			}
		}

		// This is a solution
		log.debug("Solution at {} : {} {} {} {} {} {}", perm, indexByType[0], indexByType[1], indexByType[2],
				indexByType[3], indexByType[4], indexByType[5]);

		synchronized (this) {
			EquipmentSet set = new EquipmentSet();
			for (int t = 0; t < EquipmentType.values().length; t++) {
				set.add(candidateList.getCandidates(EquipmentType.values()[t]).get(indexByType[t]));
			}
			searchResult.addSolution(set);
		}

		return searchResult.getSolutions().size() >= MAX_RESULTS;
	}

	protected int[] calculateIndexByType(int perm) {
		int[] indexByType = new int[EquipmentType.values().length];
		int permProduct = 1;
		for (int i = 0; i < EquipmentType.values().length; i++) {
			indexByType[i] = perm / permProduct % lengthByType[i];
			permProduct *= lengthByType[i];
		}
		return indexByType;
	}
}
