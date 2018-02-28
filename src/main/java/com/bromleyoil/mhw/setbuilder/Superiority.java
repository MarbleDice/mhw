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

	public static Superiority compare(SkillSet a, SkillSet b, Set<Skill> requiredSkills) {
		boolean isBetter = false;
		boolean isWorse = false;

		for (Skill skill : requiredSkills) {
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

	public static Superiority compare(SkillSet a, SkillSet b) {
		Set<Skill> allSkills = new HashSet<>(a.getSkills());
		allSkills.addAll(b.getSkills());
		return compare(a, b, allSkills);
	}

	public static Superiority compare(Equipment a, Equipment b, Set<Skill> requiredSkills) {
		return combine(compare(a.getSkillSet(), b.getSkillSet(), requiredSkills),
				compare(a.getSlotSet(), b.getSlotSet()));
	}

	public static Superiority compare(Equipment a, Equipment b) {
		Set<Skill> allSkills = new HashSet<>(a.getSkillSet().getSkills());
		allSkills.addAll(b.getSkillSet().getSkills());
		return compare(a, b, allSkills);
	}

	public static Superiority compare(EquipmentSet a, EquipmentSet b, Set<Skill> requiredSkills) {
		return combine(compare(a.getSkillSet(), b.getSkillSet(), requiredSkills),
				compare(a.getSlotSet(), b.getSlotSet()));
	}

	public static Superiority compare(EquipmentSet a, EquipmentSet b) {
		Set<Skill> allSkills = new HashSet<>(a.getSkillSet().getSkills());
		allSkills.addAll(b.getSkillSet().getSkills());
		return compare(a, b, allSkills);
	}

	protected static Superiority combine(Superiority superiority1, Superiority superiority2) {
		if (superiority1 == INCOMPARABLE || superiority2 == INCOMPARABLE) {
			return INCOMPARABLE;
		} else if (superiority1 == BETTER && superiority2 == WORSE || superiority1 == WORSE && superiority2 == BETTER) {
			return INCOMPARABLE;
		} else if (superiority1 == BETTER || superiority2 == BETTER) {
			return BETTER;
		} else if (superiority1 == WORSE || superiority2 == WORSE) {
			return WORSE;
		}
		return EQUAL;
	}
}
