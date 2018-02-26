package com.bromleyoil.mhw.model;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.bromleyoil.mhw.DataParser;

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
			items = DataParser.parseEquipment(DataParser.openResource("equipment.tsv"));
			items.addAll(DataParser.parseCharms(DataParser.openResource("charms.tsv")));
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
