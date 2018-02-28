package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.EquipmentType.*;
import static com.bromleyoil.mhw.setbuilder.Superiority.*;

import java.util.ArrayList;
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

	public List<EquipmentSet> search(EquipmentList equipmentList, SkillSet requiredSkillSet) {
		List<EquipmentSet> solutions = new ArrayList<>();
		CandidateList candidateList = new CandidateList(equipmentList, requiredSkillSet);

		log.info("Searching {} possibilities", candidateList.size());
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
								Superiority sup = Superiority.compare(set.getSkillSet(), requiredSkillSet,
										requiredSkillSet.getSkills());
								if (sup == BETTER || sup == EQUAL) {
									solutions.add(set);
								}
							}
						}
					}
				}
			}
		}
		log.info("Found {} solutions", solutions.size());
		return solutions;
	}
}
