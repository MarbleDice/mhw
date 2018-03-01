package com.bromleyoil.mhw.form;

import java.util.ArrayList;
import java.util.List;

import com.bromleyoil.mhw.model.Skill;

public class SetBuilderForm {

	private Skill newSkill;

	private List<Skill> skills = new ArrayList<>();
	private List<Integer> levels = new ArrayList<>();
	private Integer requiredSlots3;
	private Integer requiredSlots2;
	private Integer requiredSlots1;

	public Skill getNewSkill() {
		return newSkill;
	}

	public void setNewSkill(Skill newSkill) {
		this.newSkill = newSkill;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public List<Integer> getLevels() {
		return levels;
	}

	public void setLevels(List<Integer> requiredSkillLevels) {
		this.levels = requiredSkillLevels;
	}

	/**
	 * Adds a skill with the given level, sorting the skills and inserting the level at the same index in the parallel
	 * list. This is only well behaved when elements are not added through any other means.
	 * 
	 * @param skill
	 * @param level
	 */
	public void addSkillLevel(Skill skill, Integer level) {
		skills.add(skill);
		skills.sort(Skill.NAME_ORDER);
		levels.add(skills.indexOf(skill), level);
	}

	public Integer getRequiredSlots3() {
		return requiredSlots3;
	}

	public void setRequiredSlots3(Integer requiredSlots3) {
		this.requiredSlots3 = requiredSlots3;
	}

	public Integer getRequiredSlots2() {
		return requiredSlots2;
	}

	public void setRequiredSlots2(Integer requiredSlots2) {
		this.requiredSlots2 = requiredSlots2;
	}

	public Integer getRequiredSlots1() {
		return requiredSlots1;
	}

	public void setRequiredSlots1(Integer requiredSlots1) {
		this.requiredSlots1 = requiredSlots1;
	}
}
