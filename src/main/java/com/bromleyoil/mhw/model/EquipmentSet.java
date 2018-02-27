package com.bromleyoil.mhw.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class EquipmentSet {

	private Map<EquipmentType, Equipment> equipmentSet = new TreeMap<>();

	public Equipment add(Equipment equipment) {
		return equipmentSet.put(equipment.getType(), equipment);
	}

	public Set<Entry<EquipmentType, Equipment>> entrySet() {
		return equipmentSet.entrySet();
	}
}
