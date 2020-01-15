package com.bromleyoil.mhw.model;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Decoration {

	private int id;
	private int order;
	private String name;
	private int level;
	private int rarity;
	private Rank rank;
	private SkillSet skillSet = new SkillSet();
	private boolean isWildcard;

	public static final Comparator<Decoration> LEVEL_ORDER = Comparator.comparing(Decoration::getLevel)
			.thenComparing(Comparator.comparing(Decoration::isWildcard))
			.thenComparing(Comparator.comparing(Decoration::isCombination))
			.thenComparing(Comparator.comparing(Decoration::getPoints))
			.thenComparing(Comparator.comparing(Decoration::getName));

	public boolean isCombination() {
		return skillSet.getSkills().size() > 1;
	}

	public int getPoints() {
		return skillSet.getSkillLevels().stream().collect(Collectors.summingInt(Entry::getValue));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public SkillSet getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(SkillSet skillSet) {
		this.skillSet = skillSet;
	}

	public boolean isWildcard() {
		return isWildcard;
	}

	public void setWildcard(boolean isWildcard) {
		this.isWildcard = isWildcard;
	}
}
