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
		if (skill != null && level > 0) {
			skillLevels.put(skill, skillLevels.getOrDefault(skill, 0) + level);
		}
	}

	public void add(SkillSet skillSet) {
		for (Entry<Skill, Integer> entry : skillSet.skillLevels.entrySet()) {
			add(entry.getKey(), entry.getValue());
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
					difference.skillLevels.remove(skill);
				} else {
					difference.skillLevels.put(skill, minuendLevel - subtrahendLevel);
				}
			}
		}

		return difference;
	}

	public boolean contains(Skill skill) {
		return skillLevels.containsKey(skill);
	}

	public int getLevel(Skill skill) {
		return skillLevels.getOrDefault(skill, 0);
	}

	public Set<Skill> getSkills() {
		return skillLevels.keySet();
	}

	public List<Entry<Skill, String>> getSkillLabels() {
		return skillLevels.entrySet().stream()
				.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
				.map(x -> new SimpleEntry<>(x.getKey(), x.getValue().toString()))
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
