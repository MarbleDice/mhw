package com.bromleyoil.mhw.setbuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillValue;

public class CandidateListTest {

	private EquipmentList equipmentList;

	@Test
	public void buildCandidates_noSkills_onlySlotted() throws IOException {
		equipmentList = new EquipmentList();
		equipmentList.postConstruct();
		CandidateList candidateList = new CandidateList(equipmentList, new ArrayList<>());

		System.out.println("Candidates");
		for (EquipmentType type : EquipmentType.values()) {
			System.out.println("  " + type + ": " + candidateList.getCandidates(type).size());
			for (Equipment equipment : candidateList.getCandidates(type)) {
				System.out.println("    " + equipment.getFullDescription());
			}
		}
	}

	@Test
	public void buildCandidates_greatSword_() throws IOException {
		equipmentList = new EquipmentList();
		equipmentList.postConstruct();
		CandidateList candidateList = new CandidateList(equipmentList,
				Arrays.asList(new SkillValue(Skill.EARPLUGS, 1), new SkillValue(Skill.FOCUS, 1),
						new SkillValue(Skill.WEAKNESS_EXPLOIT, 1)));

		System.out.println("Candidates");
		for (EquipmentType type : EquipmentType.values()) {
			System.out.println("  " + type + ": " + candidateList.getCandidates(type).size());
			for (Equipment equipment : candidateList.getCandidates(type)) {
				System.out.println("    " + equipment.getFullDescription());
			}
		}
	}
}
