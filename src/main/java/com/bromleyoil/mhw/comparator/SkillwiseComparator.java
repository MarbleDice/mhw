package com.bromleyoil.mhw.comparator;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;

public class SkillwiseComparator implements Comparator<SkillSet> {

	private Set<Skill> interestingSkills = null;

	public static final SkillwiseComparator ALL_SKILLS = new SkillwiseComparator();

	private SkillwiseComparator() {
	}

	private SkillwiseComparator(Set<Skill> interestingSkills) {
		this.interestingSkills = interestingSkills;
	}

	public static SkillwiseComparator of(SkillSet skillSet) {
		return new SkillwiseComparator(skillSet.getSkills());
	}

	@Override
	public int compare(SkillSet a, SkillSet b) {
		Set<Skill> skills = interestingSkills != null ? interestingSkills : combineSkills(a, b);

		int rv;
		for (Skill skill : skills) {
			rv = Comparators.DESCENDING.compare(a.getLevel(skill), b.getLevel(skill));
			if (rv != 0) {
				return rv;
			}
		}

		return 0;
	}

	protected Set<Skill> combineSkills(SkillSet a, SkillSet b) {
		Set<Skill> skills = new TreeSet<>();
		skills.addAll(a.getSkills());
		skills.addAll(b.getSkills());
		return skills;
	}
}
