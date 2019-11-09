package com.bromleyoil.mhw.setbuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
import com.bromleyoil.mhw.model.SetBonus;
import com.bromleyoil.mhw.model.Skill;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ParallelSetBuilder implements SetBuilder {

	private static final Logger log = LoggerFactory.getLogger(ParallelSetBuilder.class);
	private static final int MAX_RESULTS = 100;

	@Autowired
	private EquipmentList equipmentList;

	private CandidateList candidateList;

	private FlatMultiArray setBonusSearch;
	private FlatMultiArray equipmentSearch;

	private byte[] goal;

	private SearchResult searchResult = new SearchResult();

	@Override
	public SearchResult search(SetBuilderForm form) {
		candidateList = new CandidateList(equipmentList);
		candidateList.buildCandidates(form);

		searchResult.setCandidateList(candidateList);

		goal = FlatMultiArray.convertToByteArray(form.getLevels());

		// Build the set bonus search array
		setBonusSearch = new FlatMultiArray(SetBonus.values().size(), SetBonus.values().size() * 6,
				form.getSkills().size());
		for (int i = 0; i < SetBonus.values().size(); i++) {
			setBonusSearch.addCategory(i, getRecord(SetBonus.values().get(i), form.getSkills())
					.collect(Collectors.toList()));
		}

		// Build the decorations search array

		// Build the main equipment search array. Record length is number of skills, plus two set bonus and slots.
		equipmentSearch = new FlatMultiArray(EquipmentType.values().length, candidateList.size(), form.getSkills().size() + 2);
		for (int t = 0; t < EquipmentType.values().length; t++) {
			equipmentSearch.addCategory(t, candidateList.getCandidates(EquipmentType.values()[t]).stream()
					.flatMap(e -> getRecord(e, form.getSkills()))
					.collect(Collectors.toList()));
		}

		log.debug("Goal : {}", goal);

		// Perform the parallel search
		IntStream.range(0, candidateList.getPermutationCount()).parallel().anyMatch(this::checkSolution);

		return searchResult;
	}

	private static Stream<Integer> getRecord(Equipment equipment, List<Skill> skills) {
		return Stream.concat(skills.stream().map(s -> equipment.getSkillSet().getLevel(s)),
				Stream.of(equipment.getSetBonus().getIndex(), 0));
	}

	private static Stream<Integer> getRecord(SetBonus setBonus, List<Skill> skills) {
		return IntStream.range(0, 5).mapToObj(Integer::valueOf)
				.flatMap(n -> skills.stream().map(s -> setBonus.getLevel(s, n)));
	}

	private boolean checkSolution(int perm) {
		int[] indexByType = equipmentSearch.getIndexesForPerm(perm);

		// Check if every skill in the goal is met
		for (int s = 0; s < goal.length; s++) {
			byte total = 0;
			// Add skill points from equipment
			for (int t = 0; t < EquipmentType.values().length; t++) {
				total += equipmentSearch.getField(t, indexByType[t], s);
			}

			// Add skill points from set bonuses
			for (int i = 0; i < setBonusSearch.getNumCategories(); i++) {
				// Check the number of times this set bonus appears in the equipment permutation
				int numPieces = 0;
				for (int t = 0; t < EquipmentType.values().length; t++) {
					numPieces += i == equipmentSearch.getField(t, indexByType[t], goal.length) ? 1 : 0;
				}

				total += setBonusSearch.getField(i, numPieces, s);
			}

			// Goal not met, abort
			if (total < goal[s]) {
				return false;
			}
		}

		// This is a solution, so add it to the results
		log.debug("Solution at {} : {} {} {} {} {} {}", perm, indexByType[0], indexByType[1], indexByType[2],
				indexByType[3], indexByType[4], indexByType[5]);

		synchronized (this) {
			EquipmentSet set = new EquipmentSet();
			for (int t = 0; t < EquipmentType.values().length; t++) {
				set.add(candidateList.getCandidates(EquipmentType.values()[t]).get(indexByType[t]));
			}
			searchResult.addSolution(set);
		}

		// Return true to trigger short-circuit evaluation with anyMatch
		return searchResult.getSolutions().size() >= MAX_RESULTS;
	}
}
