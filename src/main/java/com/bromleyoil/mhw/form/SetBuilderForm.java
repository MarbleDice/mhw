package com.bromleyoil.mhw.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bromleyoil.mhw.model.Rank;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;

public class SetBuilderForm {

	private Skill newSkill;

	private List<SkillRow> skillRows = new ArrayList<>();
	private Integer weaponSlots4;
	private Integer weaponSlots3;
	private Integer weaponSlots2;
	private Integer weaponSlots1;
	private Integer requiredSlots4;
	private Integer requiredSlots3;
	private Integer requiredSlots2;
	private Integer requiredSlots1;
	private Rank maxRank;
	private String algorithm;

	public Skill getNewSkill() {
		return newSkill;
	}

	public void setNewSkill(Skill newSkill) {
		this.newSkill = newSkill;
	}

	public void addSkillRow(SkillRow skillRow) {
		skillRows.add(skillRow);
		skillRows.sort((a, b) -> Skill.NAME_ORDER.compare(a.getSkill(), b.getSkill()));
	}

	public List<SkillRow> getSkillRows() {
		return skillRows;
	}

	public void setSkillRows(List<SkillRow> skillRows) {
		this.skillRows = skillRows;
	}

	public List<Skill> getSkills() {
		return skillRows.stream().map(SkillRow::getSkill).collect(Collectors.toList());
	}

	public List<Integer> getLevels() {
		return skillRows.stream().map(x -> x.getLevel() != null ? x.getLevel() : 0).collect(Collectors.toList());
	}

	public SkillSet getRequiredSkillSet() {
		return new SkillSet(getSkills(), getLevels());
	}

	public boolean contains(Skill skill) {
		return skillRows.stream().anyMatch(x -> x.getSkill() == skill);
	}

	public Integer getWeaponSlots4() {
		return weaponSlots4;
	}

	public void setWeaponSlots4(Integer weaponSlots4) {
		this.weaponSlots4 = weaponSlots4;
	}

	public Integer getWeaponSlots3() {
		return weaponSlots3;
	}

	public void setWeaponSlots3(Integer weaponSlots3) {
		this.weaponSlots3 = weaponSlots3;
	}

	public Integer getWeaponSlots2() {
		return weaponSlots2;
	}

	public void setWeaponSlots2(Integer weaponSlots2) {
		this.weaponSlots2 = weaponSlots2;
	}

	public Integer getWeaponSlots1() {
		return weaponSlots1;
	}

	public void setWeaponSlots1(Integer weaponSlots1) {
		this.weaponSlots1 = weaponSlots1;
	}

	public SlotSet getWeaponSlotSet() {
		return SlotSet.of(getWeaponSlots1(), getWeaponSlots2(), getWeaponSlots3(), getWeaponSlots4());
	}

	public Integer getRequiredSlots4() {
		return requiredSlots4;
	}

	public void setRequiredSlots4(Integer requiredSlots4) {
		this.requiredSlots4 = requiredSlots4;
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

	public SlotSet getRequiredSlotSet() {
		return SlotSet.of(getRequiredSlots1(), getRequiredSlots2(), getRequiredSlots3(), getRequiredSlots4());
	}

	public Rank getMaxRank() {
		return maxRank;
	}

	public void setMaxRank(Rank maxRank) {
		this.maxRank = maxRank;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public Map<Skill, Integer> getDecorationCounts() {
		return skillRows.stream().collect(Collectors.toMap(SkillRow::getSkill,
				x -> x.getDecorationCount() != null ? x.getDecorationCount() : 0));
	}
}
