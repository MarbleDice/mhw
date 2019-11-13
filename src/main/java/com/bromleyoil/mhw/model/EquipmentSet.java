package com.bromleyoil.mhw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.mhw.UrlCodec;

public class EquipmentSet {

	private Map<EquipmentType, Equipment> equipmentMap = new EnumMap<>(EquipmentType.class);
	private SkillSet skillSet = new SkillSet();
	private SlotSet weaponSlotSet = SlotSet.NONE;
	private SlotSet slotSet = SlotSet.NONE;
	private SlotSet filledSlotSet = SlotSet.NONE;
	private Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);

	public void add(Equipment equipment) {
		if (equipmentMap.containsKey(equipment.getType())) {
			throw new IllegalArgumentException("Set already has an item in slot " + equipment.getType());
		}

		equipmentMap.put(equipment.getType(), equipment);
		skillSet.add(equipment.getSkillSet());
		slotSet = slotSet.add(equipment.getSlotSet());

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
		SlotSet openSlots = slotSet.subtract(filledSlotSet);
		if (openSlots.getSlotCountForDeco(skill.getDecorationLevel()) > 0) {
			decorationCounts.compute(skill, (k, v) -> v != null ? v + 1 : 1);
			skillSet.add(skill, 1);
			filledSlotSet = filledSlotSet.add(openSlots.getMinSlotLevel(skill.getDecorationLevel()));
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
				.collect(Collectors.joining(", ")) + (hasSlots() ? " " + slotSet.getLabel() : ""));
		return String.join("\n", lines);
	}

	public String getWeaponLabel() {
		return hasWeaponSlots() ? "Slotted Weapon " + getWeaponSlotSet().getLabel() : "Any weapon";
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

	public boolean hasSlots() {
		return slotSet.getSlotCountForDeco(1) > 0;
	}

	public boolean hasWeaponSlots() {
		return weaponSlotSet.getSlotCountForDeco(1) > 0;
	}

	public SlotSet getWeaponSlotSet() {
		return weaponSlotSet;
	}

	public void setWeaponSlotSet(SlotSet weaponSlotSet) {
		this.weaponSlotSet = weaponSlotSet;
		slotSet = slotSet.add(weaponSlotSet);
	}

	public SlotSet getSlotSet() {
		return slotSet;
	}

	public void setSlotSet(SlotSet slotSet) {
		this.slotSet = slotSet;
	}

	public String getSlotLabel() {
		return Stream.of(filledSlotSet.getFilledLabel(), slotSet.subtract(filledSlotSet).getLabel())
				.filter(s -> !StringUtils.isBlank(s))
				.collect(Collectors.joining(" "));
	}
}
