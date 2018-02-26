package com.bromleyoil.mhw.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Equipment {

	private String setName;
	private EquipmentType type;
	private Set<SkillValue> skills = new TreeSet<>(SkillValue.POINT_ORDER);
	private List<Integer> slots = new ArrayList<>();

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

	public static String getSlotLabel(Integer slotLevel) {
		if (slotLevel == 1) {
			return "①";
		} else if (slotLevel == 2) {
			return "②";
		} else if (slotLevel == 3) {
			return "③";
		}
		return "?";
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
		skills.add(new SkillValue(skill, pointValue));
	}

	public boolean hasSkill(Skill skill) {
		return skills.stream().anyMatch(x -> skill.equals(x.getSkill()));
	}

	public Set<SkillValue> getSkills() {
		return skills;
	}

	public void setSkills(Set<SkillValue> skills) {
		this.skills = skills;
	}

	public void addSlot(int level) {
		slots.add(level);
	}

	public List<Integer> getSlots() {
		return slots;
	}

	public List<String> getSlotLabels() {
		return slots.stream().map(Equipment::getSlotLabel).collect(Collectors.toList());
	}

	public void setSlots(List<Integer> slots) {
		this.slots = slots;
	}
}
