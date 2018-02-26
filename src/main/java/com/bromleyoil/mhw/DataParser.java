package com.bromleyoil.mhw;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.PointValue;
import com.bromleyoil.mhw.model.Skill;

public class DataParser {

	protected static final String NAME = "Name";
	protected static final String HEAD = "Head";
	protected static final String BODY = "Body";
	protected static final String HANDS = "Hands";
	protected static final String WAIST = "Waist";
	protected static final String LEGS = "Legs";
	protected static final String SET2 = "Set2";
	protected static final String SET3 = "Set3";
	protected static final String SET4 = "Set4";

	protected static final String SKILL1 = "Skill1";
	protected static final String SKILL2 = "Skill2";
	protected static final String MAX = "Max";

	protected static final Pattern descriptionPattern = Pattern.compile("\\s*([a-zA-Z0-9'/, -]+)\\s*(\\(.+\\))?\\s*");
	protected static final Pattern skillPattern = Pattern.compile("\\s*(.+)\\s+(\\d+)\\s*");

	private DataParser() {
		// Static class
	}

	public static Reader openResource(String resourceName) {
		return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
	}

	public static List<String> parseSkills(Reader reader) throws IOException {
		Map<String, List<String>> skills = new TreeMap<>();

		String currentSkill = null;
		List<String> skillDescriptions = new ArrayList<>();
		for (CSVRecord record : CSVFormat.TDF.parse(reader)) {
			String newSkill = record.get(0);
			String skillDescription = record.get(1);

			if (StringUtils.isBlank(skillDescription)) {
				continue;
			}

			// Starting a new skill
			if (!StringUtils.isBlank(newSkill)) {
				// There was a skill before this one
				if (currentSkill != null) {
					skills.put(currentSkill, skillDescriptions);
					skillDescriptions = new ArrayList<>();
				}

				currentSkill = newSkill;
			}

			// Add the description to the list for the current skill
			skillDescriptions.add(
					skillDescription.replace("Lv1,", "").replace("Lv2,", "").replace("Lv3,", "").replace("Lv4,", "")
							.replace("Lv5,", "").replace("Lv6,", "").replace("Lv7,", "").replace("\"", ""));
		}
		skills.put(currentSkill, skillDescriptions);

		return printEnumInitializers(skills);
	}

	protected static List<String> printEnumInitializers(Map<String, List<String>> skills) {
		List<String> lines = new ArrayList<>();
		for (Entry<String, List<String>> skill : skills.entrySet()) {
			// Start the enum initializer block
			lines.add(String.format("\t%s(\"%s\",", Skill.getEnumName(skill.getKey()), skill.getKey()));

			// Determine the terminator for the last description of this skill
			String lastDescriptionTerminator;
			if (skill.getKey() == ((SortedSet<String>) skills.keySet()).last()) {
				lastDescriptionTerminator = ");";
			} else {
				lastDescriptionTerminator = "),";
			}

			// New line for each skill level description
			for (String description : skill.getValue()) {
				// Determine the description terminator
				String descriptionTerminator;
				if (description == skill.getValue().get(skill.getValue().size() - 1)) {
					descriptionTerminator = lastDescriptionTerminator;
				} else {
					descriptionTerminator = ",";
				}

				lines.add(String.format("\t\t\"%s\"%s", description, descriptionTerminator));
			}
		}
		return lines;
	}

	public static List<Equipment> parseEquipment(Reader reader) throws IOException {
		List<Equipment> equipment = new ArrayList<>();
		for (CSVRecord record : CSVFormat.TDF.withFirstRecordAsHeader().parse(reader)) {
			if (!StringUtils.isBlank(record.get(HEAD))) {
				equipment.add(createEquip(record, EquipmentType.HEAD, record.get(HEAD)));
			}
			if (!StringUtils.isBlank(record.get(BODY))) {
				equipment.add(createEquip(record, EquipmentType.BODY, record.get(BODY)));
			}
			if (!StringUtils.isBlank(record.get(HANDS))) {
				equipment.add(createEquip(record, EquipmentType.HANDS, record.get(HANDS)));
			}
			if (!StringUtils.isBlank(record.get(WAIST))) {
				equipment.add(createEquip(record, EquipmentType.WAIST, record.get(WAIST)));
			}
			if (!StringUtils.isBlank(record.get(LEGS))) {
				equipment.add(createEquip(record, EquipmentType.LEGS, record.get(LEGS)));
			}
		}
		return equipment;
	}

	protected static Equipment createEquip(CSVRecord record, EquipmentType type, String description) {
		Equipment equipment = new Equipment();

		// Name
		equipment.setArmorName(record.get(NAME));
		equipment.setType(type);

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
		if (!StringUtils.isBlank(record.get(SET2))) {
			equipment.addSkill(Skill.valueOfName(record.get(SET2)), PointValue.SET2);
		}

		if (!StringUtils.isBlank(record.get(SET3))) {
			equipment.addSkill(Skill.valueOfName(record.get(SET3)), PointValue.SET3);
		}

		if (!StringUtils.isBlank(record.get(SET4))) {
			equipment.addSkill(Skill.valueOfName(record.get(SET4)), PointValue.SET4);
		}

		return equipment;
	}

	public static List<Equipment> parseCharms(Reader reader) throws IOException {
		List<Equipment> charms = new ArrayList<>();
		for (CSVRecord record : CSVFormat.TDF.withFirstRecordAsHeader().parse(reader)) {
			Equipment charm = new Equipment();

			int maxLevel = Integer.parseInt(record.get(MAX));
			for (int level = 1; level <= maxLevel; level++) {
				String suffix = String.join("", Collections.nCopies(level, "I"));
				charm.setName(record.get(NAME) + (maxLevel == 1 ? "" : " " + suffix));

				Skill skill;
				skill = Skill.valueOfName(record.get(SKILL1));
				charm.addSkill(skill, PointValue.valueOf(level));
				if (!StringUtils.isBlank(record.get(SKILL2))) {
					skill = Skill.valueOfName(record.get(SKILL2));
					charm.addSkill(skill, PointValue.valueOf(level));
				}
				charms.add(charm);
			}
		}
		return charms;
	}

	protected static void addSkills(Equipment equipment, String skills) {
		for (String skill : skills.split(",")) {
			skill = skill.trim();
			if (skill.equals("-")) {
				continue;
			}
			Matcher matcher = skillPattern.matcher(skill);
			if (matcher.matches()) {
				equipment.addSkill(Skill.valueOfName(matcher.group(1)),
						PointValue.valueOf(Integer.valueOf(matcher.group(2))));
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
