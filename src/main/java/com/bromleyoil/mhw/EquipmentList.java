package com.bromleyoil.mhw;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class EquipmentList {

	private List<Equipment> items;

	public EquipmentList(List<Equipment> items) {
		this.items = items;
		Collections.sort(items, Equipment.ARMOR_NAME_AND_TYPE_ORDER);
	}

	public EquipmentList filter(EquipmentType type) {
		return new EquipmentList(items.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList()));
	}

	public EquipmentList filter(Skill skill) {
		return new EquipmentList(items.stream().filter(x -> x.hasSkill(skill)).collect(Collectors.toList()));
	}

	public List<Equipment> getItems() {
		return items;
	}

	public Map<String, Equipment[]> getMatrix() {
		Map<String, Equipment[]> matrix = new TreeMap<>();

		for (Equipment equipment : items) {
			if (equipment.getType() == EquipmentType.CHARM) {
				continue;
			}
			if (!matrix.containsKey(equipment.getArmorName())) {
				matrix.put(equipment.getArmorName(), new Equipment[5]);
			}
			matrix.get(equipment.getArmorName())[equipment.getType().ordinal()] = equipment;
		}

		return matrix;
	}
}
