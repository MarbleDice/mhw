package com.bromleyoil.mhw.model;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.bromleyoil.mhw.parser.DataParser;

@Component
public class EquipmentList {

	private List<Equipment> items;

	public EquipmentList() {
	}

	public EquipmentList(List<Equipment> items) {
		this.items = items;
	}

	@PostConstruct
	public void postConstruct() throws IOException {
		if (items == null) {
			items = DataParser.parseAllEquipment();
		}
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

	public Equipment find(String armorName, EquipmentType type) {
		return items.stream().filter(x -> x.getArmorName().equals(armorName) && x.getType() == type)
				.findFirst().orElse(null);
	}

	public Equipment find(String name) {
		return items.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
	}

	public Map<String, Equipment[]> getMatrix() {
		Map<String, Equipment[]> matrix = new LinkedHashMap<>();

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
