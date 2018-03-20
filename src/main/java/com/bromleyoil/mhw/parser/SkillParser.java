package com.bromleyoil.mhw.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

public class SkillParser {
	private static final Pattern decorationPattern = Pattern.compile("\\s+([123])$");

	public static Reader openResource(String resourceName) {
		return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName),
				StandardCharsets.UTF_8);
	}

	public static List<String> getEnumInitializerLines() throws IOException {
		Map<String, Skill> skills = parseSkills(openResource("skills.tsv"));
		parseDecorations(skills, openResource("decorations.tsv"));
		return createLines(skills.values());
	}

	protected static Map<String, Skill> parseSkills(Reader reader) throws IOException {
		Map<String, Skill> skills = new TreeMap<>();

		Skill currentSkill = null;
		for (CSVRecord record : CSVFormat.TDF.parse(reader)) {
			String skillName = record.get(0);
			String skillDescription = record.get(1);

			if (StringUtils.isBlank(skillDescription)) {
				continue;
			}

			// Start a new skill
			if (!StringUtils.isBlank(skillName)) {
				currentSkill = new Skill();
				currentSkill.name = skillName;
				skills.put(currentSkill.name, currentSkill);
			}

			// Add the description to the list for the current skill
			currentSkill.descriptions.add(skillDescription.replace("Lv1,", "").replace("Lv2,", "").replace("Lv3,", "")
					.replace("Lv4,", "").replace("Lv5,", "").replace("Lv6,", "").replace("Lv7,", "").replace("\"", ""));
		}

		return skills;
	}

	protected static void parseDecorations(Map<String, Skill> skills, Reader reader) throws IOException {
		for (CSVRecord record : CSVFormat.TDF.parse(reader)) {
			String decorationName = record.get(0);
			String skillName = record.get(1);
			int decorationRarity = Integer.parseInt(record.get(2));

			if (StringUtils.isBlank(skillName)) {
				continue;
			}

			Skill skill = skills.get(skillName);

			if (skill != null) {
				skill.decorationName = decorationName;
				skill.decorationLevel = parseDecorationLevel(decorationName);
				skill.decorationRarity = decorationRarity;
			} else {
				throw new IllegalArgumentException("Unknown skill: " + skillName);
			}

		}
	}

	protected static int parseDecorationLevel(String decorationName) {
		Matcher matcher = decorationPattern.matcher(decorationName);
		if (matcher.find() && matcher.groupCount() == 1) {
			return Integer.valueOf(matcher.group(1));
		} else {
			throw new IllegalArgumentException("Invalid decoration name: " + decorationName);
		}
	}

	protected static List<String> createLines(Collection<Skill> skills) {
		List<String> lines = new ArrayList<>();
		Skill lastSkill = skills.stream().reduce((x, y) -> y).get();
		for (Skill skill : skills) {
			// Start the enum initializer block
			lines.add(String.format("\t%s(\"%s\", \"%s\", %d, %d,",
					com.bromleyoil.mhw.model.Skill.getEnumName(skill.name), skill.name,
					skill.decorationName, skill.decorationLevel, skill.decorationRarity));

			// Determine the terminator for the last description of this skill
			String lastDescriptionTerminator;
			if (skill == lastSkill) {
				lastDescriptionTerminator = ");";
			} else {
				lastDescriptionTerminator = "),";
			}

			// New line for each skill level description
			for (String description : skill.descriptions) {
				// Determine the description terminator
				String descriptionTerminator;
				if (description == skill.descriptions.get(skill.descriptions.size() - 1)) {
					descriptionTerminator = lastDescriptionTerminator;
				} else {
					descriptionTerminator = ",";
				}

				lines.add(String.format("\t\t\"%s\"%s", description, descriptionTerminator));
			}
		}
		return lines;
	}

	private static class Skill {
		private String name;
		private List<String> descriptions = new ArrayList<>();
		private String decorationName = "";
		private int decorationLevel;
		private int decorationRarity;
	}
}
