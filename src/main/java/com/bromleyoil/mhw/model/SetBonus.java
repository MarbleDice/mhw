package com.bromleyoil.mhw.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class SetBonus {

	private static final Map<String, SetBonus> SET_BONUSES = new TreeMap<>();

	public static final SetBonus NONE = SetBonus.of("");

	private String name;
	private Map<Integer, Skill> pieceSkills = new TreeMap<>();
	private Map<Skill, Integer> skillPieces = new TreeMap<>();

	private SetBonus() {
		this.name = "";
	}

	private SetBonus(String name, Skill... skills) {
		this.name = name;
		for (int i = 0; i < skills.length; i++) {
			if (skills[i] != null) {
				pieceSkills.put(i + 2, skills[i]);
				skillPieces.put(skills[i], i + 2);
			}
		}
	}

	public static SetBonus of(String name, Skill... skills) {
		SET_BONUSES.computeIfAbsent(name, n -> new SetBonus(n, skills));
		return SET_BONUSES.get(name);
	}

	public static List<SetBonus> values() {
		return SET_BONUSES.values().stream().collect(Collectors.toList());
	}

	public int getIndex() {
		return values().indexOf(this);
	}

	public String getName() {
		return name;
	}

	/**
	 * @return A mapping of number of pieces to the skill bonus granted.
	 */
	public Map<Integer, Skill> getSkills() {
		return pieceSkills;
	}

	/**
	 * Gets the skill in this bonus for the given number of equipment pieces.
	 * 
	 * @param numPieces
	 * @return
	 */
	public Skill getSkill(int numPieces) {
		return pieceSkills.get(numPieces);
	}

	public Integer getLevel(Skill skill, int numPieces) {
		return skillPieces.containsKey(skill) && numPieces >= skillPieces.get(skill) ? 1 : 0;
	}

	public boolean hasSkill(Skill skill) {
		return pieceSkills.containsValue(skill);
	}

	public int getNumPieces(Skill skill) {
		return skillPieces.containsKey(skill) ? skillPieces.get(skill) : 999;
	}
}
