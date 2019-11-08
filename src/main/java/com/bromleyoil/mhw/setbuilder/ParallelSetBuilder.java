package com.bromleyoil.mhw.setbuilder;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.EquipmentType;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ParallelSetBuilder implements SetBuilder {

	private static final Logger log = LoggerFactory.getLogger(ParallelSetBuilder.class);
	private static final int MAX_RESULTS = 100;

	@Autowired
	private EquipmentList equipmentList;

	private CandidateList candidateList;

	private FlatMultiArray search;

	private byte[] goal;

	private SearchResult searchResult = new SearchResult();

	@Override
	public SearchResult search(SetBuilderForm form) {
		candidateList = new CandidateList(equipmentList);
		candidateList.buildCandidates(form);

		searchResult.setCandidateList(candidateList);

		goal = FlatMultiArray.convertToByteArray(form.getLevels());

		search = new FlatMultiArray(EquipmentType.values().length, candidateList.size(), form.getSkills().size());
		for (int t = 0; t < EquipmentType.values().length; t++) {
			search.addCategory(t, candidateList.getCandidates(EquipmentType.values()[t]).stream()
					.flatMap(e -> form.getSkills().stream().map(s -> e.getSkillSet().getLevel(s)))
					.collect(Collectors.toList()));
		}

		log.debug("Goal : {}", goal);

		IntStream.range(0, candidateList.getPermutationCount()).parallel().anyMatch(this::checkSolution);

		return searchResult;
	}

	private boolean checkSolution(int perm) {
		int[] indexByType = search.getIndexesForPerm(perm);

		// TODO needs set bonuses
		for (int s = 0; s < search.getRecordLength(); s++) {
			byte total = 0;
			for (int t = 0; t < EquipmentType.values().length; t++) {
				total += search.getField(t, indexByType[t], s);
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
}
