package com.bromleyoil.mhw.setbuilder;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;

public class CandidateListTest {

	private EquipmentList equipmentList;

	@Ignore
	@Test
	public void buildCandidates_noSkills_onlySlotted() throws IOException {
		equipmentList = new EquipmentList();
		equipmentList.postConstruct();
		CandidateList candidateList = new CandidateList(equipmentList, new SkillSet());

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
		CandidateList candidateList = new CandidateList(equipmentList, new SkillSet(
				Arrays.asList(Skill.EARPLUGS, Skill.FOCUS, Skill.WEAKNESS_EXPLOIT),
				Arrays.asList(5, 3, 3)));

		System.out.println("Candidates");
		for (EquipmentType type : EquipmentType.values()) {
			System.out.println("  " + type + ": " + candidateList.getCandidates(type).size());
			for (Equipment equipment : candidateList.getCandidates(type)) {
				System.out.println("    " + equipment.getFullDescription());
			}
		}
	}
}
