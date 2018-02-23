package com.bromleyoil.mhw;

import java.util.Map;
import java.util.TreeMap;

public class Equipment {

	private String name;
	private EquipmentType type;
	private Map<Skill, PointValue> skills = new TreeMap<>(Skill.NAME_ORDER);
	private int[] slots = new int[3];

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void addSlot(int level) {
		slots[level - 1]++;
	}

	public Map<Skill, PointValue> getSkills() {
		return skills;
	}

	public void setSkills(Map<Skill, PointValue> skills) {
		this.skills = skills;
	}

	public int[] getSlots() {
		return slots;
	}

	public void setSlots(int[] slots) {
		this.slots = slots;
	}
}
