package com.bromleyoil.mhw.model;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

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

	public EquipmentSet decode(String encodedString) {
		byte[] bytes = Base64Utils.decodeFromUrlSafeString(encodedString);
		EquipmentSet equipmentSet = new EquipmentSet();

		int equipmentLoaded = 0;
		for (int i = 0; i < bytes.length; i += 2) {
			if (equipmentLoaded < EquipmentType.values().length) {
				// Load a 2-byte equipment ID
				int id = convertToInt(bytes, i, 2);
				if (id > 0) {
					equipmentSet.add(items.get(id - 1));
				}

				equipmentLoaded++;
			} else {
				// Load a decoration skill and count
				equipmentSet.decorate(Skill.values()[convertToInt(bytes, i, 1)], convertToInt(bytes, i + 1, 1));
			}
		}

		return equipmentSet;
	}

	protected static int convertToInt(byte[] bytes, int index, int length) {
		return new BigInteger(Arrays.copyOfRange(bytes, index, index + length)).intValue();
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
