package com.bromleyoil.mhw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
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
	private Map<Decoration, Integer> decorationCounts = new HashMap<>();

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
	 * Tries to add a single decoration into the equipment set.
	 * 
	 * @param decoration
	 */
	public void decorate(Decoration decoration) {
		SlotSet openSlots = getOpenSlotSet();
		if (openSlots.getSlotCountForDeco(decoration.getLevel()) > 0) {
			decorationCounts.compute(decoration, (k, v) -> v != null ? v + 1 : 1);
			skillSet.add(decoration.getSkillSet());
			filledSlotSet = filledSlotSet.add(openSlots.getMinSlotLevel(decoration.getLevel()));
		}
	}

	/**
	 * Tries to add multiple decorations of the given type into the equipment set.
	 * 
	 * @param decoration
	 * @param count
	 */
	public void decorate(Decoration decoration, int count) {
		for (int i = 0; i < count; i++) {
			decorate(decoration);
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

	public List<Entry<Decoration, Integer>> getDecorationCounts() {
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

	public SlotSet getOpenSlotSet() {
		return slotSet.subtract(filledSlotSet);
	}

	public void setSlotSet(SlotSet slotSet) {
		this.slotSet = slotSet;
	}

	public String getSlotLabel() {
		return Stream.of(filledSlotSet.getFilledLabel(), getOpenSlotSet().getLabel())
				.filter(s -> !StringUtils.isBlank(s))
				.collect(Collectors.joining(" "));
	}
}
