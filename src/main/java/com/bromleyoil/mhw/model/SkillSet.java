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
		addByValue(skill, fraction.getValue());
	}

	public void add(SkillSet skillSet) {
		for (Entry<Skill, Integer> entry : skillSet.skillValues.entrySet()) {
			addByValue(entry.getKey(), entry.getValue());
		}
	}

	protected void addByValue(Skill skill, int value) {
		if (contains(skill)) {
			skillValues.put(skill, skillValues.get(skill) + value);
		} else {
			skillValues.put(skill, value);
		}

		int maxValue = Fraction.getNumerator(skill.getMaxLevel());
		if (skillValues.get(skill) > maxValue) {
			skillValues.put(skill, maxValue);
		}
	}

	
	public SkillSet subtract(SkillSet subtrahend) {
		SkillSet difference = new SkillSet(skillValues.keySet(), skillValues.values());
		for (Entry<Skill, Integer> entry : subtrahend.skillValues.entrySet()) {
			Skill skill = entry.getKey();
			int subtrahendValue = entry.getValue();
			int minuendValue = difference.getValue(skill);
			if (difference.contains(skill)) {
				if (difference.getValue(skill) <= subtrahendValue) {
					difference.skillValues.remove(skill);
				} else {
					difference.skillValues.put(skill, minuendValue - subtrahendValue);
				}
			}
		}
		return difference;
	}

	public boolean contains(Skill skill) {
		return skillValues.containsKey(skill);
	}

	public int getLevel(Skill skill) {
		return contains(skill) ? Fraction.getLevel(skillValues.get(skill)) : 0;
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
				.filter(x -> Fraction.getLevel(x.getValue()) > 0)
				.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
				.map(x -> new SimpleEntry<>(x.getKey(), Fraction.getLevel(x.getValue())))
				.collect(Collectors.toList());
	}
}
