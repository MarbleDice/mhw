package com.bromleyoil.mhw.model;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

public class Equipment {

	private String name;
	private String armorName;
	private EquipmentType type;
	private Set<SkillValue> skills = new TreeSet<>(SkillValue.POINT_ORDER);
	private SlotSet slots = new SlotSet();

	public static final Comparator<Equipment> ARMOR_NAME_AND_TYPE_ORDER = (a, b) -> {
		int rv = a.getArmorName().compareTo(b.getArmorName());
		if (rv == 0) {
			rv = a.getType().compareTo(b.getType());
		}
		return rv;
	};

	@Override
	public String toString() {
		return armorName + " " + type;
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

	public SlotSet getSlots() {
		return slots;
	}

	public void setSlots(SlotSet slots) {
		this.slots = slots;
	}
}
