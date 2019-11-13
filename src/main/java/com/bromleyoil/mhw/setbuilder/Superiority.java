package com.bromleyoil.mhw.setbuilder;

import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.SetBonus;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;

public enum Superiority {
	WORSE, EQUAL, BETTER, INCOMPARABLE;

	/** A collector that combines Superiority into one aggregate rating. */
	private static final Collector<Superiority, ?, Superiority> COMBINING_COLLECTOR = Collectors.reducing(EQUAL,
			Superiority::combine);

	/**
	 * Returns a superiority value for the given compareTo return value, where 0 is EQUAL, 1 is BETTER, and -1 is WORSE.
	 * 
	 * @param compareValue
	 * @return
	 */
	public static Superiority valueOf(int compareValue) {
		if (compareValue < 0) {
			return WORSE;
		} else if (compareValue > 0) {
			return BETTER;
		} else {
			return EQUAL;
		}
	}

	/**
	 * Compares two slot sets.
	 * 
	 * @param a
	 * @param b
	 * @return BETTER if a is better than b, WORSE if it is worse, or EQUAL, or INCOMPARABLE.
	 */
	public static Superiority compare(SlotSet a, SlotSet b) {
		return IntStream.rangeClosed(1, SlotSet.MAX_SLOT_LEVEL)
				.map(level -> Integer.compare(a.getSlotCountForDeco(level), b.getSlotCountForDeco(level)))
				.mapToObj(Superiority::valueOf)
				.collect(COMBINING_COLLECTOR);
	}

	/**
	 * Compares skill levels on two skill sets, using only the interesting skills.
	 * 
	 * @param a
	 * @param b
	 * @param interestingSkills
	 * @return BETTER if a is better than b, WORSE if it is worse, or EQUAL, or INCOMPARABLE.
	 */
	public static Superiority compare(SkillSet a, SkillSet b, Set<Skill> interestingSkills) {
		return interestingSkills.stream()
				.mapToInt(s -> Integer.compare(a.getLevel(s), b.getLevel(s)))
				.mapToObj(Superiority::valueOf)
				.collect(COMBINING_COLLECTOR);
	}

	/**
	 * Compares skill levels on two skill sets.
	 * 
	 * @param a
	 * @param b
	 * @return BETTER if a is better than b, WORSE if it is worse, or EQUAL, or INCOMPARABLE.
	 */
	public static Superiority compare(SkillSet a, SkillSet b) {
		return Stream.concat(a.getSkills().stream(), b.getSkills().stream())
				.mapToInt(s -> Integer.compare(a.getLevel(s), b.getLevel(s)))
				.mapToObj(Superiority::valueOf)
				.collect(COMBINING_COLLECTOR);
	}

	/**
	 * Compares two set bonuses.
	 * 
	 * @param a
	 * @param b
	 * @param interestingSkills
	 * @return BETTER if a is better than b, WORSE if it is worse, or EQUAL, or INCOMPARABLE.
	 */
	public static Superiority compare(SetBonus a, SetBonus b, Set<Skill> interestingSkills) {
		return interestingSkills.stream()
				.mapToInt(s -> 0 - Integer.compare(a.getNumPieces(s), b.getNumPieces(s)))
				.mapToObj(Superiority::valueOf)
				.collect(COMBINING_COLLECTOR);
	}

	/**
	 * Checks if the skill levels of the first skill set are equal to or better than the levels of the second one.
	 * 
	 * @param a
	 * @param b
	 * @return True if a has equal or higher levels for all skills in b, false otherwise.
	 */
	public static boolean equalOrBetter(SkillSet a, SkillSet b) {
		for (Skill skill : b.getSkills()) {
			if (a.getLevel(skill) < b.getLevel(skill)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if the slots of the first slot set are equal to or better than the slots of the second one.
	 * 
	 * @param a
	 * @param b
	 * @return True if a has equal or more slots than b, false otherwise.
	 */
	public static boolean equalOrBetter(SlotSet a, SlotSet b) {
		// Short-circuit if a is worse than b
		return IntStream.rangeClosed(1, SlotSet.MAX_SLOT_LEVEL)
				.allMatch(level -> a.getSlotCountForDeco(level) >= b.getSlotCountForDeco(level));
	}

	/**
	 * Compares two pieces of equipment, using only the interesting skills.
	 * 
	 * NOTE: Does not consider that set bonuses could appear as skill points.
	 * 
	 * @param a
	 * @param b
	 * @param interestingSkills
	 * @return BETTER if a is better than b, WORSE if it is worse, or EQUAL, or INCOMPARABLE.
	 */
	public static Superiority compare(Equipment a, Equipment b, Set<Skill> interestingSkills) {
		return a.getType() != b.getType() ? INCOMPARABLE
				: combine(compare(a.getSkillSet(), b.getSkillSet(), interestingSkills),
						compare(a.getSetBonus(), b.getSetBonus(), interestingSkills),
						compare(a.getSlotSet(), b.getSlotSet()));
	}

	/**
	 * Compares two equipment sets.
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
