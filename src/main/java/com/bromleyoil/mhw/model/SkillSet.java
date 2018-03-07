package com.bromleyoil.mhw.model;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

public class SkillSet {

	private Map<Skill, Integer> skillValues = new EnumMap<>(Skill.class);
	private Map<Skill, Integer> skillLevels = new EnumMap<>(Skill.class);

	public SkillSet() {
	}

	public SkillSet(Skill skill, int level) {
		add(skill, level);
	}

	public SkillSet(Collection<Skill> skills, Collection<Integer> levels) {
		Assert.isTrue(skills.size() == levels.size(), "Number of skills and levels must match");
		Iterator<Skill> skillIter = skills.iterator();
		Iterator<Integer> levelIter = levels.iterator();
		while (skillIter.hasNext()) {
			add(skillIter.next(), levelIter.next());
		}
	}

	public void add(Skill skill, int level) {
		addByValue(skill, Fraction.getNumerator(level));
	}

	public void add(Skill skill, Fraction fraction) {
		addByValue(skill, fraction.getNumerator());
	}

	public void add(SkillSet skillSet) {
		for (Entry<Skill, Integer> entry : skillSet.skillValues.entrySet()) {
			addByValue(entry.getKey(), entry.getValue());
		}
	}

	protected void addByValue(Skill skill, int value) {
		int newSkillValue = Integer.min(skillValues.getOrDefault(skill, 0) + value,
				Fraction.getNumerator(skill.getMaxLevel()));
		skillValues.put(skill, newSkillValue);

		int newSkillLevel = Fraction.getQuotient(newSkillValue);
		if (newSkillLevel > 0) {
			skillLevels.put(skill, newSkillLevel);
		}
	}

	public SkillSet subtractByLevel(SkillSet subtrahend) {
		SkillSet difference = new SkillSet();
		getSkillLevels().stream().filter(x -> x.getValue() > 0).forEach(x -> difference.add(x.getKey(), x.getValue()));

		for (Entry<Skill, Integer> entry : subtrahend.getSkillLevels()) {
			Skill skill = entry.getKey();
			int subtrahendLevel = entry.getValue();
			int minuendLevel = difference.getLevel(skill);
			if (difference.contains(skill)) {
				if (minuendLevel <= subtrahendLevel) {
					difference.skillValues.remove(skill);
					difference.skillLevels.remove(skill);
				} else {
					difference.skillValues.put(skill, Fraction.getNumerator(minuendLevel - subtrahendLevel));
					difference.skillLevels.put(skill, minuendLevel - subtrahendLevel);
				}
			}
		}
		return difference;
	}

	public boolean contains(Skill skill) {
		return skillValues.containsKey(skill);
	}

	public int getLevel(Skill skill) {
		return skillLevels.getOrDefault(skill, 0);
	}

	public int getValue(Skill skill) {
		return skillValues.getOrDefault(skill, 0);
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

	public Set<Entry<Skill, Integer>> getSkillLevels() {
		return skillLevels.entrySet();
	}

	public List<Entry<Skill, Integer>> getOrderedSkillLevels() {
		return skillLevels.entrySet().stream()
				.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
				.collect(Collectors.toList());
	}
}
