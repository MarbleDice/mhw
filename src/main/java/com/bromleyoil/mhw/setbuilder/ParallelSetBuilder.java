package com.bromleyoil.mhw.setbuilder;

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
import com.bromleyoil.mhw.model.EquipmentType;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ParallelSetBuilder implements SetBuilder {

	private Logger log = LoggerFactory.getLogger(ParallelSetBuilder.class);

	@Autowired
	private EquipmentList equipmentList;

	private CandidateList candidateList;

	private byte[] search;
	private int[] lengthByType = new int[EquipmentType.values().length];
	private int[] offsetByType = new int[EquipmentType.values().length];

	public ParallelSetBuilder() {

	}

	@Override
	public SearchResult search(SetBuilderForm form) {
		candidateList = new CandidateList(equipmentList);
		candidateList.buildCandidates(form);

		int recordLength = form.getSkills().size();
		search = new byte[candidateList.size() * recordLength];

		offsetByType = new int[EquipmentType.values().length];
		for (int i = 0; i < EquipmentType.values().length; i++) {
			lengthByType[i] = candidateList.getCandidates(EquipmentType.values()[i]).size();
			offsetByType[i] = i == 0 ? 0 : offsetByType[i - 1] + lengthByType[i - 1] * recordLength;
		}

		// candidateList.getPermutationCount() and .parallel()
		IntStream.range(0, 1000).anyMatch(this::checkSolution);

		return new SearchResult();
	}

	private boolean checkSolution(int perm) {
		int[] indexByType = calculateIndexByType(perm);
		int[] solution = new int[indexByType.length];

		// log.info("{} : {} {} {} {} {} {}", perm, indexByType[0], indexByType[1], indexByType[2], indexByType[3],
		// indexByType[4], indexByType[5]);

		for (int i = 0; i < indexByType.length; i++) {
			
		}

		return true;
	}

	protected int[] calculateIndexByType(int perm) {
		int[] indexByType = new int[lengthByType.length];
		int permProduct = 1;
		for (int i = 0; i < indexByType.length; i++) {
			indexByType[i] = perm / permProduct % lengthByType[i];
			permProduct *= lengthByType[i];
		}
		return indexByType;
	}
}
