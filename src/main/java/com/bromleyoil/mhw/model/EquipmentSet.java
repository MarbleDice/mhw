package com.bromleyoil.mhw.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.util.Base64Utils;

public class EquipmentSet {

	private Map<EquipmentType, Equipment> equipmentMap = new EnumMap<>(EquipmentType.class);
	private SkillSet skillSet = new SkillSet();
	private SlotSet slotSet = new SlotSet();
	private Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);

	public void add(Equipment equipment) {
		if (equipmentMap.containsKey(equipment.getType())) {
			throw new IllegalArgumentException("Set already has an item in slot " + equipment.getType());
		}

		equipmentMap.put(equipment.getType(), equipment);
		skillSet.add(equipment.getSkillSet());
		slotSet.add(equipment.getSlotSet());
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

	public String getBase64() {
		List<Integer> ints = new ArrayList<>();

		// Add the ID for each piece of equipment, or 0 if missing
		for (EquipmentType type : EquipmentType.values()) {
			ints.add(equipmentMap.get(type) != null ? equipmentMap.get(type).getId() : 0);
		}

		// Add the ID and count for each decorated skill
		for (Entry<Skill, Integer> entry : decorationCounts.entrySet()) {
			ints.add(entry.getKey().ordinal());
			ints.add(entry.getValue());
		}

		// Convert the IDs to a list of bytes, padding the equipment to 2 byte values
		List<Byte> bytes = new ArrayList<>();
		for (int i = 0; i < ints.size(); i++) {
			bytes.addAll(padBytes(ints.get(i), i < EquipmentType.values().length ? 2 : 0));
		}

		// Convert the list of bytes to a primitive array of bytes
		byte[] byteArray = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++) {
			byteArray[i] = bytes.get(i);
		}

		return Base64Utils.encodeToUrlSafeString(byteArray);
	}

	protected List<Byte> padBytes(int value, int padLength) {
		List<Byte> bytes = new ArrayList<>();

		for (byte b : BigInteger.valueOf(value).toByteArray()) {
			bytes.add(b);
		}

		while (bytes.size() < padLength) {
			bytes.add(0, (byte) 0);
		}

		return bytes;
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
}
