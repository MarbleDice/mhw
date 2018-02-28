package com.bromleyoil.mhw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class EquipmentSet {

	private Map<EquipmentType, Equipment> equipmentMap = new TreeMap<>();
	private SkillSet skillSet = new SkillSet();
	private SlotSet slotSet = new SlotSet();

	public void add(Equipment equipment) {
		if (equipmentMap.containsKey(equipment.getType())) {
			throw new IllegalArgumentException("Set already has an item in slot " + equipment.getType());
		}

		equipmentMap.put(equipment.getType(), equipment);
		skillSet.add(equipment.getSkillSet());
		slotSet.add(equipment.getSlotSet());
	}

	public SkillSet getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(SkillSet skillSet) {
		this.skillSet = skillSet;
	}

	public SlotSet getSlotSet() {
		return slotSet;
	}

	public void setSlotSet(SlotSet slotSet) {
		this.slotSet = slotSet;
	}

	@Override
	public String toString() {
		return getShortDescription();
	}

	public String getShortDescription() {
		return equipmentMap.values().stream().map(Equipment::getArmorName).collect(Collectors.joining("|"));
	}

	public String getLongDescription() {
		List<String> lines = new ArrayList<>();
		lines.add("Set:");
		for (Equipment equipment : equipmentMap.values()) {
			lines.add("\t" + equipment.getFullDescription());
		}
		lines.add("Total: " + skillSet.getSkillLevels().stream().map(x -> x.getKey() + " " + x.getValue())
				.collect(Collectors.joining(", ")) + (!slotSet.isEmpty() ? " " + slotSet.getAsciiLabel() : ""));
		return String.join("\n", lines);
	}
}
