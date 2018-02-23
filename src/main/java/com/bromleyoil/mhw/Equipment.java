package com.bromleyoil.mhw;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Equipment {

	private String setName;
	private EquipmentType type;
	private Map<Skill, PointValue> skills = new TreeMap<>(Skill.NAME_ORDER);
	private int[] slots = new int[3];

	public static final Comparator<Equipment> ARMOR_NAME_AND_TYPE_ORDER = (a, b) -> {
		int rv = a.getArmorName().compareTo(b.getArmorName());
		if (rv == 0) {
			rv = a.getType().compareTo(b.getType());
		}
		return rv;
	};

	@Override
	public String toString() {
		return setName + " " + type;
	}

	public String getArmorName() {
		return setName;
	}

	public void setArmorName(String name) {
		this.setName = name;
	}

	public EquipmentType getType() {
		return type;
	}

	public void setType(EquipmentType type) {
		this.type = type;
	}

	public void addSkill(Skill skill, PointValue pointValue) {
		skills.put(skill, pointValue);
	}

	public boolean hasSkill(Skill skill) {
		return skills.keySet().contains(skill);
	}

	public Map<Skill, PointValue> getSkills() {
		return skills;
	}

	public void setSkills(Map<Skill, PointValue> skills) {
		this.skills = skills;
	}

	public void addSlot(int level) {
		slots[level - 1]++;
	}

	public int[] getSlots() {
		return slots;
	}

	public void setSlots(int[] slots) {
		this.slots = slots;
	}
}
