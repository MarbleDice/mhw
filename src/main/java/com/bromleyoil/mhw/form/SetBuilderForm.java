package com.bromleyoil.mhw.form;

import java.util.ArrayList;
import java.util.List;

import com.bromleyoil.mhw.model.Skill;

public class SetBuilderForm {

	private List<Skill> interestingSkills = new ArrayList<>();
	private List<Integer> requiredSkillLevels = new ArrayList<>();
	private int requiredSlots3;
	private int requiredSlots2;
	private int requiredSlots1;

	public List<Skill> getInterestingSkills() {
		return interestingSkills;
	}

	public void setInterestingSkills(List<Skill> interestingSkills) {
		this.interestingSkills = interestingSkills;
	}

	public List<Integer> getRequiredSkillLevels() {
		return requiredSkillLevels;
	}

	public void setRequiredSkillLevels(List<Integer> requiredSkillLevels) {
		this.requiredSkillLevels = requiredSkillLevels;
	}

	public int getRequiredSlots3() {
		return requiredSlots3;
	}

	public void setRequiredSlots3(int requiredSlots3) {
		this.requiredSlots3 = requiredSlots3;
	}

	public int getRequiredSlots2() {
		return requiredSlots2;
	}

	public void setRequiredSlots2(int requiredSlots2) {
		this.requiredSlots2 = requiredSlots2;
	}

	public int getRequiredSlots1() {
		return requiredSlots1;
	}

	public void setRequiredSlots1(int requiredSlots1) {
		this.requiredSlots1 = requiredSlots1;
	}
}
