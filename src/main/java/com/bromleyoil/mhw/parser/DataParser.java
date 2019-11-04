package com.bromleyoil.mhw.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.mhw.model.Decoration;
import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Rank;
import com.bromleyoil.mhw.model.SetBonus;
import com.bromleyoil.mhw.model.Skill;

public class DataParser {

	// Iceborne parser
	protected static final String SET_NAME = "Set Name";
	protected static final String TYPE = "Type";
	protected static final String ITEM_NAME = "Item Name";
	protected static final String SKILLS = "Skills";
	protected static final String SLOTS = "Slots";
	protected static final String SET_BONUS_NAME = "Set Bonus Name";
	protected static final String SET_BONUS_2 = "Set Bonus 2";
	protected static final String SET_BONUS_3 = "Set Bonus 3";
	protected static final String SET_BONUS_4 = "Set Bonus 4";
	protected static final String SET_BONUS_5 = "Set Bonus 5";

	// Old parser
	protected static final String NAME = "Name";
	protected static final String HEAD = "Head";
	protected static final String BODY = "Body";
	protected static final String HANDS = "Hands";
	protected static final String WAIST = "Waist";
	protected static final String LEGS = "Legs";
	protected static final String SET2 = "Set2";
	protected static final String SET3 = "Set3";
	protected static final String SET4 = "Set4";

	// Charm parsing
	protected static final String SKILL1 = "Skill1";
	protected static final String SKILL2 = "Skill2";
	protected static final String POINTS = "Points";
	protected static final String RANK = "Rank";
	protected static final String RARITY = "Rarity";
	protected static final String LEVEL = "Level";
	protected static final Pattern descriptionPattern = Pattern.compile("\\s*([a-zA-Z0-9'/, -]+)\\s*(\\(.+\\))?\\s*");
	protected static final Pattern skillPattern = Pattern.compile("\\s*(.+)\\s+(\\d+)\\s*");

	private DataParser() {
		// Static class
	}

	public static Reader openResource(String resourceName) {
		return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName),
				StandardCharsets.UTF_8);
	}

	public static List<Equipment> parseAllEquipment() throws IOException {
		List<Equipment> items = DataParser.parseEquipment(DataParser.openResource("master-rank.tsv"));
		items.addAll(DataParser.parseCharms(DataParser.openResource("charms.tsv")));
		// Set IDs
		for (int i = 0; i < items.size(); i++) {
			items.get(i).setId(i + 1);
		}
		return items;
	}

	protected static List<Equipment> parseEquipment(Reader reader) throws IOException {
		List<Equipment> equipment = new ArrayList<>();
		for (CSVRecord record : CSVFormat.TDF.withFirstRecordAsHeader().parse(reader)) {
			equipment.add(createEquip(record));
		}
		return equipment;
	}

	protected static Equipment createEquip(CSVRecord record) {
		Equipment equipment = new Equipment();

		// Name
		equipment.setArmorName(record.get(SET_NAME));
		equipment.setType(EquipmentType.valueOf(record.get(TYPE).toUpperCase()));

		// Skills
		addSkills(equipment, record.get(SKILLS));

		// Slots
		Optional.ofNullable(record.get(SLOTS)).orElse("").codePoints()
				.mapToObj(c -> Integer.valueOf(String.valueOf((char) c)))
				.forEach(equipment::addSlot);

		// Set bonus skills
		if (!StringUtils.isBlank(record.get(SET_BONUS_NAME))) {
			equipment.setSetBonus(new SetBonus(record.get(SET_BONUS_NAME),
					Skill.valueOfName(record.get(SET_BONUS_2)),
					Skill.valueOfName(record.get(SET_BONUS_3)),
					Skill.valueOfName(record.get(SET_BONUS_4)),
					Skill.valueOfName(record.get(SET_BONUS_5))));
		}

		return equipment;
	}

	protected static List<Equipment> parseOldEquipment(Reader reader) throws IOException {
		List<Equipment> equipment = new ArrayList<>();
		for (CSVRecord record : CSVFormat.TDF.withFirstRecordAsHeader().parse(reader)) {
			if (!StringUtils.isBlank(record.get(HEAD))) {
				equipment.add(createOldEquip(record, EquipmentType.HEAD, record.get(HEAD)));
			}
			if (!StringUtils.isBlank(record.get(BODY))) {
				equipment.add(createOldEquip(record, EquipmentType.CHEST, record.get(BODY)));
			}
			if (!StringUtils.isBlank(record.get(HANDS))) {
				equipment.add(createOldEquip(record, EquipmentType.ARM, record.get(HANDS)));
			}
			if (!StringUtils.isBlank(record.get(WAIST))) {
				equipment.add(createOldEquip(record, EquipmentType.WAIST, record.get(WAIST)));
			}
			if (!StringUtils.isBlank(record.get(LEGS))) {
				equipment.add(createOldEquip(record, EquipmentType.LEG, record.get(LEGS)));
			}
		}
		return equipment;
	}

	protected static Equipment createOldEquip(CSVRecord record, EquipmentType type, String description) {
		Equipment equipment = new Equipment();

		// Name
		equipment.setArmorName(record.get(NAME));
		equipment.setType(type);
		if (!StringUtils.isBlank(record.get(RANK))) {
			equipment.setRank(Rank.valueOf(record.get(RANK)));
		}

		// Parse skills and slots
		Matcher matcher = descriptionPattern.matcher(description);
		if (matcher.matches()) {
			// Skills
			addSkills(equipment, matcher.group(1));

			// Slots
			if (matcher.groupCount() > 1 && !StringUtils.isBlank(matcher.group(2))) {
				addSlots(equipment, matcher.group(2));
			}
		} else {
			throw new IllegalArgumentException("Invalid description string: " + description);
		}

		// Set bonus skills
		if (!StringUtils.isBlank(record.get(SET_BONUS_NAME))) {
			equipment.setSetBonus(new SetBonus(record.get(SET_BONUS_NAME),
					Skill.valueOfName(record.get(SET_BONUS_2)),
					Skill.valueOfName(record.get(SET_BONUS_3)),
					Skill.valueOfName(record.get(SET_BONUS_4)),
					null));
		}

		return equipment;
	}

	protected static List<Equipment> parseCharms(Reader reader) throws IOException {
		List<Equipment> charms = new ArrayList<>();
		for (CSVRecord record : CSVFormat.TDF.withFirstRecordAsHeader().parse(reader)) {
			int level = Integer.parseInt(record.get(POINTS));
			Equipment charm = new Equipment();

			charm.setName(record.get(NAME));
			charm.setArmorName(charm.getName());
			charm.setType(EquipmentType.CHARM);
			if (!StringUtils.isBlank(record.get(RANK))) {
				charm.setRank(Rank.valueOf(record.get(RANK)));
			}

			Skill skill = Skill.valueOfName(record.get(SKILL1));
			charm.addSkill(skill, level);
			if (!StringUtils.isBlank(record.get(SKILL2))) {
				skill = Skill.valueOfName(record.get(SKILL2));
				charm.addSkill(skill, level);
			}

			charms.add(charm);
		}
		return charms;
	}

	public static List<Decoration> parseAllDecorations() throws IOException {
		List<Decoration> decorations = new ArrayList<>();
		Reader reader = DataParser.openResource("decorations.tsv");
		for (CSVRecord record : CSVFormat.TDF.withFirstRecordAsHeader().parse(reader)) {
			decorations.add(createDecoration(record));
		}

		// Set IDs
		for (int i = 0; i < decorations.size(); i++) {
			decorations.get(i).setId(i + 1);
		}

		return decorations;
	}

	protected static Decoration createDecoration(CSVRecord record) {
		Decoration deco = new Decoration();

		deco.setName(record.get(NAME));
		deco.setLevel(Integer.parseInt(record.get(LEVEL)));
		if (!StringUtils.isBlank(record.get(RARITY))) {
			deco.setRarity(Integer.parseInt(record.get(RARITY)));
		}
		deco.getSkillSet().add(Skill.valueOfName(record.get(SKILL1)), Integer.parseInt(record.get(POINTS)));
		if (!StringUtils.isBlank(record.get(SKILL2))) {
			deco.getSkillSet().add(Skill.valueOfName(record.get(SKILL2)), Integer.parseInt(record.get(POINTS)));
		}

		return deco;
	}

	protected static void addSkills(Equipment equipment, String skills) {
		for (String skill : skills.split(",")) {
			skill = skill.trim();
			if (skill.equals("-")) {
				continue;
			}
			Matcher matcher = skillPattern.matcher(skill);
			if (matcher.matches()) {
				equipment.addSkill(Skill.valueOfName(matcher.group(1)), Integer.valueOf(matcher.group(2)));
			} else {
				throw new IllegalArgumentException("Invalid skill string: " + skills);
			}
		}
	}

	protected static void addSlots(Equipment equipment, String slots) {
		slots = slots.replace("(", "").replace(")", "");
		for (String slot : slots.split(",")) {
			slot = slot.trim();
			equipment.addSlot(Integer.valueOf(slot));
		}
	}
}
