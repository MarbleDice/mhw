package com.bromleyoil.mhw.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private List<Decoration> decorations;

	/** Generic "any" equipment for each slot */
	private Map<EquipmentType, Equipment> anyEquipment;

	/** The MH:W base game decorations for each skill */
	private Map<Skill, Decoration> baseDecorations;

	public EquipmentList() {
		anyEquipment = Arrays.stream(EquipmentType.values())
				.map(EquipmentList::createDefaultEquipment)
				.collect(Collectors.toMap(Equipment::getType, e -> e));
	}

	public EquipmentList(List<Equipment> items, List<Decoration> decorations) {
		this();
		this.items = items;
		this.decorations = decorations;
	}

	@PostConstruct
	public void postConstruct() throws IOException {
		if (items == null) {
			items = DataParser.parseAllEquipment();
		}
		if (decorations == null) {
			decorations = DataParser.parseAllDecorations();
			baseDecorations = decorations.stream()
					.filter(d -> d.getLevel() < 4)
					.collect(Collectors.toMap(d -> d.getSkillSet().getOrderedSkillLevels().get(0).getKey(), d -> d));
		}
	}

	private static Equipment createDefaultEquipment(EquipmentType type) {
		Equipment equipment = new Equipment();
		equipment.setId(0);
		equipment.setType(type);
		equipment.setName("Any " + type.getDescription());
		return equipment;
	}

	/**
	 * Gets a generic "any piece of equipment" for the given slot.
	 * 
	 * @param type
	 * @return
	 */
	public Equipment getAny(EquipmentType type) {
		return anyEquipment.get(type);
	}

	public EquipmentList filter(EquipmentType type) {
		EquipmentList rv = new EquipmentList();
		rv.items = items.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
		rv.decorations = new ArrayList<>(decorations);
		return rv;
	}

	public EquipmentList filter(Skill skill) {
		EquipmentList rv = new EquipmentList();
		rv.items = items.stream().filter(x -> x.hasSkill(skill)).collect(Collectors.toList());
		rv.decorations = decorations.stream().filter(x -> x.getSkillSet().contains(skill)).collect(Collectors.toList());
		return rv;
	}

	public List<Equipment> getItems() {
		return items;
	}

	public List<Decoration> getDecorations() {
		return decorations;
	}

	/**
	 * Gets all base decorations
	 * 
	 * @return
	 */
	public Map<Skill, Decoration> getBaseDecorations() {
		return baseDecorations;
	}

	/**
	 * Gets a MH:W base game per-skill decoration for the given skill.
	 * 
	 * @param skill
	 * @return
	 */
	public Decoration getBaseDecoration(Skill skill) {
		return baseDecorations.get(skill);
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
			if (!matrix.containsKey(equipment.getNbspArmorName())) {
				matrix.put(equipment.getNbspArmorName(), new Equipment[5]);
			}
			matrix.get(equipment.getNbspArmorName())[equipment.getType().ordinal()] = equipment;
		}

		return matrix;
	}
}
