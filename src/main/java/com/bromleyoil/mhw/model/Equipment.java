package com.bromleyoil.mhw.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Equipment {

	private int id;
	private String name;
	private String armorName;
	private EquipmentType type;
	private SkillSet skillSet = new SkillSet();
	private SlotSet slotSet = SlotSet.NONE;
	private SetBonus setBonus = SetBonus.NONE;
	private Rank rank;

	public static final Comparator<Equipment> ARMOR_NAME_AND_TYPE_ORDER = (a, b) -> {
		int rv = a.getArmorName().compareTo(b.getArmorName());
		if (rv == 0) {
			rv = a.getType().compareTo(b.getType());
		}
		return rv;
	};

	@Override
	public String toString() {
		return getName();
	}

	public String getNameAndSlots() {
		return getName() + (hasSlots() ? " " + slotSet.getLabel() : "");
	}

	public String getFullDescription() {
		return getName() + " | " + skillSet.getSkillLabels().stream().map(x -> x.getKey() + " " + x.getValue())
				.collect(Collectors.joining(", ")) + (hasSlots() ? " " + slotSet.getLabel() : "");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return StringUtils.isBlank(name) ? armorName + " " + type : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArmorName() {
		return armorName;
	}

	public void setArmorName(String name) {
		this.armorName = name;
	}

	public String getNbspArmorName() {
		return armorName.replace(" +", " +")
				.replace(" α", " α")
				.replace(" β", " β");
	}

	public EquipmentType getType() {
		return type;
	}

	public void setType(EquipmentType type) {
		this.type = type;
	}

	public void addSkill(Skill skill, int level) {
		skillSet.add(skill, level);
	}

	public boolean hasSkill(Skill skill) {
		return skillSet.contains(skill) || setBonus.hasSkill(skill);
	}

	public boolean hasAnySkill(SkillSet skillSet) {
		// Non-disjoint sets mean there is overlap
		return !Collections.disjoint(this.skillSet.getSkills(), skillSet.getSkills()) ||
				!Collections.disjoint(setBonus.getSkills().values(), skillSet.getSkills());
	}

	public SkillSet getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(SkillSet skillSet) {
		this.skillSet = skillSet;
	}

	public boolean hasSlots() {
		return slotSet.hasSlots();
	}

	public SlotSet getSlotSet() {
		return slotSet;
	}

	public void setSlotSet(SlotSet slots) {
		this.slotSet = slots;
	}

	public SetBonus getSetBonus() {
		return setBonus;
	}

	public void setSetBonus(SetBonus setBonus) {
		this.setBonus = setBonus;
	}

	public boolean hasSetBonus() {
		return setBonus != null && !StringUtils.isBlank(setBonus.getName());
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}
}
