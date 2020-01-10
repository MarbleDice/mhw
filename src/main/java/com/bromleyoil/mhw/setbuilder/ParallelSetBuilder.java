package com.bromleyoil.mhw.setbuilder;

import java.util.Arrays;
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

	/* Equipment indexed by [type][equipment index][field] */
	private FlatMultiArray equipmentSearch;
	/* Set bonuses indexed by [set bonus index][num pieces][skill] */
	private FlatMultiArray setBonusSearch;
	/* Equipment indexed by [slot set][deco set index][skill] */
	private FlatMultiArray decoSearch;

	private byte[] goal;

	private SearchResult searchResult = new SearchResult();

	@Override
	public SearchResult search(SetBuilderForm form) {
		candidateList = new CandidateList(equipmentList);
		candidateList.buildCandidates(form);

		searchResult.setCandidateList(candidateList);

		goal = FlatMultiArray.convertToByteArray(form.getLevels());

		// Build the set bonus search array
		setBonusSearch = new FlatMultiArray(SetBonus.values().size(), SetBonus.values().size() * 6, goal.length);
		for (int i = 0; i < SetBonus.values().size(); i++) {
			setBonusSearch.addCategory(i, getRecord(SetBonus.values().get(i), form.getSkills())
					.collect(Collectors.toList()));
		}

		// Build the decorations search array
		decoSearch = new FlatMultiArray(1, 1, goal.length);
		decoSearch.addCategory(0, IntStream.range(0, goal.length)
				.mapToObj(i -> Integer.valueOf(0))
				.collect(Collectors.toList()));

		// Build the main equipment search array. Record length is number of skills, plus two set bonus and slots.
		equipmentSearch = new FlatMultiArray(EquipmentType.values().length, candidateList.size(), goal.length + 2);
		for (int t = 0; t < EquipmentType.values().length; t++) {
			equipmentSearch.addCategory(t, candidateList.getCandidates(EquipmentType.values()[t]).stream()
					.flatMap(e -> getRecord(e, form.getSkills()))
					.collect(Collectors.toList()));
		}

		log.debug("Goal : {}", goal);

		// Perform the parallel search
		IntStream.range(0, candidateList.getPermutationCount()).parallel().anyMatch(this::checkSolutions);

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

	private boolean checkSolutions(int perm) {
		int[] equipmentIndexes = equipmentSearch.getIndexesForPerm(perm);
		byte[] solution = new byte[goal.length];
		byte[] setBonuses = new byte[EquipmentType.values().length];

		// Add skill points from equipment and extract set bonuses
		for (int t = 0; t < EquipmentType.values().length; t++) {
			setBonuses[t] = equipmentSearch.getField(t, equipmentIndexes[t], goal.length);

			for (int s = 0; s < goal.length; s++) {
				solution[s] += equipmentSearch.getField(t, equipmentIndexes[t], s);
			}
		}

		// Add skill points from set bonuses
		Arrays.sort(setBonuses);
		int numPieces;
		for (int i = 0; i < setBonuses.length; i += numPieces) {
			numPieces = 1;

			for (int j = i + 1; j < setBonuses.length; j++) {
				if (setBonuses[i] == setBonuses[j]) {
					numPieces++;
				}
			}

			for (int s = 0; s < goal.length; s++) {
				solution[s] += setBonusSearch.getField(setBonuses[i], numPieces, s);
			}
		}

		// Check every decoration permutation for solutions
		int decoCategory = 0;
		for (int d = 0; d < decoSearch.getCategoryLength(decoCategory); d++) {
			if (checkSolution(solution, decoCategory, d)) {
				addSolution(equipmentIndexes, decoCategory, d);
			}
		}

		// Return true to trigger short-circuit evaluation with anyMatch
		return searchResult.getSolutions().size() >= MAX_RESULTS;
	}

	public boolean checkSolution(byte[] solution, int decoCategory, int decoIndex) {
		// Check if every skill in the goal is met
		for (int s = 0; s < goal.length; s++) {
			// Goal not met, abort
			if (solution[s] + decoSearch.getField(decoCategory, decoIndex, s) < goal[s]) {
				return false;
			}
		}

		// Solution found
		return true;
	}

	public void addSolution(int[] equipmentIndexes, int decoCategory, int decoIndex) {
		log.debug("Solution at: {} {} {} {} {} {}", equipmentIndexes[0], equipmentIndexes[1],
				equipmentIndexes[2], equipmentIndexes[3], equipmentIndexes[4], equipmentIndexes[5]);

		synchronized (this) {
			EquipmentSet set = new EquipmentSet();
			for (int t = 0; t < EquipmentType.values().length; t++) {
				set.add(candidateList.getCandidates(EquipmentType.values()[t]).get(equipmentIndexes[t]));
			}
			searchResult.addSolution(set);
		}
	}
}
