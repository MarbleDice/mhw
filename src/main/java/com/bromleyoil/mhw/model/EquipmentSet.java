package com.bromleyoil.mhw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.bromleyoil.mhw.UrlCodec;

public class EquipmentSet {

	private Map<EquipmentType, Equipment> equipmentMap = new EnumMap<>(EquipmentType.class);
	private SkillSet skillSet = new SkillSet();
	private SlotSet slotSet = new SlotSet();
	private SlotSet weaponSlotSet;
	private Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);

	public void add(Equipment equipment) {
		if (equipmentMap.containsKey(equipment.getType())) {
			throw new IllegalArgumentException("Set already has an item in slot " + equipment.getType());
		}

		equipmentMap.put(equipment.getType(), equipment);
		skillSet.add(equipment.getSkillSet());
		slotSet.add(equipment.getSlotSet());

		// Add the activated set bonus skill
		if (equipment.hasSetBonus()) {
			int numPieces = equipmentMap.values().stream()
					.filter(e -> e.getSetBonus().getName().equals(equipment.getSetBonus().getName()))
					.mapToInt(e -> 1).sum();
			Skill skill = equipment.getSetBonus().getSkill(numPieces);
			skillSet.add(skill, 1);
		}
	}

	/**
	 * Tries to add enough decorations to match the required skill set, without exceeding the decoration counts.
	 * 
	 * @param requiredSkillSet
	 * @param decorationCounts
	 */
	public void decorate(SkillSet requiredSkillSet, Map<Skill, Integer> decorationCounts) {
		SkillSet missingSkillSet = requiredSkillSet.subtractByLevel(getSkillSet());
		for (Entry<Skill, Integer> entry : missingSkillSet.getSkillLevels()) {
			Skill skill = entry.getKey();
			Integer levels = entry.getValue();
			if (decorationCounts.containsKey(skill) && decorationCounts.get(skill) >= levels) {
				decorate(skill, levels);
			}
		}
	}

	/**
	 * Tries to add a single decoration for the given skill into the equipment set.
	 * 
	 * @param skill
	 */
	public void decorate(Skill skill) {
		if (slotSet.hasOpenSlot(skill.getDecorationLevel())) {
			decorationCounts.compute(skill, (k, v) -> v != null ? v + 1 : 1);
			skillSet.add(skill, 1);
			slotSet.decorate(skill);
		}
	}

	/**
	 * Tries to add multiple decorations for the given skill into the equipment set.
	 * 
	 * @param skill
	 * @param levels
	 */
	public void decorate(Skill skill, int levels) {
		for (int i = 0; i < levels; i++) {
			decorate(skill);
		}
	}

	@Override
	public String toString() {
		return getShortDescription();
	}

	public String getShortDescription() {
		return getWeaponLabel() + "|" + equipmentMap.values().stream().map(Equipment::getArmorName)
				.collect(Collectors.joining("|"));
	}

	public String getLongDescription() {
		List<String> lines = new ArrayList<>();
		lines.add("Set:");
		if (hasWeaponSlots()) {
			lines.add("\t" + getWeaponLabel());
		}
		for (Equipment equipment : equipmentMap.values()) {
			lines.add("\t" + equipment.getFullDescription());
		}
		lines.add("Total: " + skillSet.getSkillLevels().stream().map(x -> x.getKey() + " " + x.getValue())
				.collect(Collectors.joining(", ")) + (!slotSet.isEmpty() ? " " + slotSet.getAsciiLabel() : ""));
		return String.join("\n", lines);
	}

	public String getWeaponLabel() {
		return hasWeaponSlots() ? "Slotted Weapon " + getWeaponSlotSet().getAsciiLabel() : "Any weapon";
	}

	public String getBase64() {
		return UrlCodec.encode(this);
	}

	public List<Entry<Skill, Integer>> getDecorationCounts() {
		return decorationCounts.entrySet().stream().collect(Collectors.toList());
	}

	public Equipment get(EquipmentType type) {
		return equipmentMap.get(type);
	}

	public Collection<Equipment> getEquipment() {
		return equipmentMap.values();
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

	public boolean hasWeaponSlots() {
		return weaponSlotSet != null && weaponSlotSet.hasSlots();
	}

	public SlotSet getWeaponSlotSet() {
		if (weaponSlotSet == null) {
			setWeaponSlotSet(new SlotSet());
		}
		return weaponSlotSet;
	}

	public void setWeaponSlotSet(SlotSet weaponSlotSet) {
		this.weaponSlotSet = weaponSlotSet;
		slotSet.add(weaponSlotSet);
	}
}
