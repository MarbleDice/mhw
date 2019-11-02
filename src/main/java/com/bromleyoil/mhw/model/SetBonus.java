package com.bromleyoil.mhw.model;

import java.util.Map;
import java.util.TreeMap;

public class SetBonus {

	public static final SetBonus NONE = new SetBonus();

	private String name;
	private Map<Integer, Skill> pieceSkills = new TreeMap<>();
	private Map<Skill, Integer> skillPieces = new TreeMap<>();

	public SetBonus() {
		this.name = "";
	}

	public SetBonus(String name, Skill... skills) {
		this.name = name;
		for (int i = 0; i < skills.length; i++) {
			if (skills[i] != null) {
				pieceSkills.put(i + 2, skills[i]);
				skillPieces.put(skills[i], i + 2);
			}
		}
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

	public boolean hasSkill(Skill skill) {
		return pieceSkills.containsValue(skill);
	}

	public int getNumPieces(Skill skill) {
		return skillPieces.containsKey(skill) ? skillPieces.get(skill) : 999;
	}
}
