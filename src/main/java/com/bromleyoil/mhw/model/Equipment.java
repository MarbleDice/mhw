package com.bromleyoil.mhw.model;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Equipment {

	private String name;
	private String armorName;
	private EquipmentType type;
	private Set<SkillValue> skillValues = new TreeSet<>(SkillValue.POINT_ORDER);
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
		return getName();
	}

	public String getNameAndSlots() {
		return getName() + (hasSlots() ? " " + slots.getAsciiLabel() : "");
	}

	public String getFullDescription() {
		return getName()
				+ " | " + skillValues.stream().map(SkillValue::toString).collect(Collectors.joining(", "))
				+ (hasSlots() ? " " + slots.getAsciiLabel() : "");
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
		skillValues.add(new SkillValue(skill, pointValue));
	}

	public boolean hasSkill(Skill skill) {
		return skillValues.stream().anyMatch(x -> skill.equals(x.getSkill()));
	}

	public int getValue(Skill skill) {
		return skillValues.stream()
				.filter(x -> x.getSkill().equals(skill))
				.map(x -> x.getPointValue().getValue())
				.findFirst().orElse(0);
	}

	public Set<SkillValue> getSkillValues() {
		return skillValues;
	}

	public void setSkillValues(Set<SkillValue> skillValues) {
		this.skillValues = skillValues;
	}

	public void addSlot(int level) {
		slots.add(level);
	}

	public boolean hasSlots() {
		return slots.getCount() > 0;
	}

	public SlotSet getSlots() {
		return slots;
	}

	public void setSlots(SlotSet slots) {
		this.slots = slots;
	}
}
