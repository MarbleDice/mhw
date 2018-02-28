package com.bromleyoil.mhw.setbuilder;

import java.util.HashSet;
import java.util.Set;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;

public enum Superiority {
	WORSE, EQUAL, BETTER, INCOMPARABLE;

	/**
	 * Compares two slot sets.
	 * 
	 * @param a
	 * @param b
	 * @return BETTER if a is better than b, WORSE if it is worse, or EQUAL, or INCOMPARABLE.
	 */
	public static Superiority compare(SlotSet a, SlotSet b) {
		int onePlus = Integer.compare(a.getOnePlus(), b.getOnePlus());
		int twoPlus = Integer.compare(a.getTwoPlus(), b.getTwoPlus());
		int three = Integer.compare(a.getThree(), b.getThree());
		if (onePlus == 0 && twoPlus == 0 && three == 0) {
			return EQUAL;
		} else if (onePlus >= 0 && twoPlus >= 0 && three >= 0) {
			return BETTER;
		} else if (onePlus <= 0 && twoPlus <= 0 && three <= 0) {
			return WORSE;
		}
		return INCOMPARABLE;
	}

	/**
	 * Compares partial skills on two sets, using only the interesting skills.
	 * 
	 * @param a
	 * @param b
	 * @param interestingSkills
	 * @return BETTER if a is better than b, WORSE if it is worse, or EQUAL, or INCOMPARABLE.
	 */
	public static Superiority compare(SkillSet a, SkillSet b, Set<Skill> interestingSkills) {
		boolean isBetter = false;
		boolean isWorse = false;

		for (Skill skill : interestingSkills) {
			isBetter |= Integer.compare(a.getValue(skill), b.getValue(skill)) > 0;
			isWorse |= Integer.compare(a.getValue(skill), b.getValue(skill)) < 0;
		}

		if (isBetter && isWorse) {
			return INCOMPARABLE;
		} else if (isBetter) {
			return BETTER;
		} else if (isWorse) {
			return WORSE;
		}
		return EQUAL;
	}

	/**
	 * Compares full skill levels on two sets.
	 * 
	 * @param a
	 * @param b
	 * @return BETTER if a is better than b, WORSE if it is worse, or EQUAL, or INCOMPARABLE.
	 */
	public static Superiority compare(SkillSet a, SkillSet b) {
		boolean isBetter = false;
		boolean isWorse = false;

		Set<Skill> allSkills = new HashSet<>(a.getSkills());
		allSkills.addAll(b.getSkills());
		for (Skill skill : allSkills) {
			isBetter |= Integer.compare(a.getLevel(skill), b.getLevel(skill)) > 0;
			isWorse |= Integer.compare(a.getLevel(skill), b.getLevel(skill)) < 0;
		}

		if (isBetter && isWorse) {
			return INCOMPARABLE;
		} else if (isBetter) {
			return BETTER;
		} else if (isWorse) {
			return WORSE;
		}
		return EQUAL;
	}

	/**
	 * Compares slots and partial skills on two pieces of equipment, using only the interesting skills.
	 * 
	 * @param a
	 * @param b
	 * @param interestingSkills
	 * @return BETTER if a is better than b, WORSE if it is worse, or EQUAL, or INCOMPARABLE.
	 */
	public static Superiority compare(Equipment a, Equipment b, Set<Skill> interestingSkills) {
		return combine(compare(a.getSkillSet(), b.getSkillSet(), interestingSkills),
				compare(a.getSlotSet(), b.getSlotSet()));
	}

	/**
	 * Compares slots and full skill levels on two equipment sets.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Superiority compare(EquipmentSet a, EquipmentSet b) {
		return combine(compare(a.getSkillSet(), b.getSkillSet()), compare(a.getSlotSet(), b.getSlotSet()));
	}

	/**
	 * Combines two or more superiority ratings into one aggregate rating.
	 * 
	 * @param superiorities
	 * @return
	 */
	protected static Superiority combine(Superiority... superiorities) {
		boolean isBetter = false;
		boolean isWorse = false;

		for (Superiority superiority : superiorities) {
			if (superiority == INCOMPARABLE) {
				return INCOMPARABLE;
			}
			isBetter |= superiority == BETTER;
			isWorse |= superiority == WORSE;
		}

		if (isBetter && isWorse) {
			return INCOMPARABLE;
		} else if (isBetter) {
			return BETTER;
		} else if (isWorse) {
			return WORSE;
		}

		return EQUAL;
	}
}
