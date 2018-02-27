package com.bromleyoil.mhw.model;

import java.util.AbstractMap.SimpleEntry;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

public class SkillSet {

	private Map<Skill, Integer> skillValues = new EnumMap<>(Skill.class);

	public SkillSet() {
	}

	public SkillSet(List<Skill> skills, List<Integer> levels) {
		Assert.isTrue(skills.size() == levels.size(), "Number of skills and levels must match");
		for (int i = 0; i < skills.size(); i++) {
			add(skills.get(i), levels.get(i));
		}
	}

	public void add(Skill skill, int level) {
		if (contains(skill)) {
			skillValues.put(skill, skillValues.get(skill) + Fraction.getNumerator(level));
		} else {
			skillValues.put(skill, Fraction.getNumerator(level));
		}
	}

	public void add(Skill skill, Fraction fraction) {
		if (contains(skill)) {
			skillValues.put(skill, skillValues.get(skill) + fraction.getValue());
		} else {
			skillValues.put(skill, fraction.getValue());
		}
	}

	public boolean contains(Skill skill) {
		return skillValues.containsKey(skill);
	}

	public int getValue(Skill skill) {
		return contains(skill) ? skillValues.get(skill) : 0;
	}

	public Set<Skill> getSkills() {
		return skillValues.keySet();
	}

	public List<Entry<Skill, String>> getSkillLabels() {
		return skillValues.entrySet().stream()
				.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
				.map(x -> new SimpleEntry<>(x.getKey(), Fraction.getLabel(x.getValue())))
				.collect(Collectors.toList());
	}

	public List<Entry<Skill, Integer>> getSkillLevels() {
		return skillValues.entrySet().stream()
				.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
				.map(x -> new SimpleEntry<>(x.getKey(), Fraction.getLevel(x.getValue())))
				.collect(Collectors.toList());
	}
}
