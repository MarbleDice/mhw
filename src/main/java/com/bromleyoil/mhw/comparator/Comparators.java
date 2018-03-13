package com.bromleyoil.mhw.comparator;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;

import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;

public class Comparators {

	public static final Comparator<Integer> DESCENDING = (a, b) -> b - a;
	public static final Comparator<SkillSet> ALL_SKILLS = new SkillwiseComparator();

	private Comparators() {
		// Static utility class
	}

	public static Comparator<SkillSet> skillwise(Set<Skill> interestingSkills) {
		return new SkillwiseComparator(interestingSkills);
	}

	public static <T, U> Comparator<T> adapted(Function<T, U> adaptor, Comparator<U> comparator) {
		return new AdaptedComparator<>(adaptor, comparator);
	}

	@SafeVarargs
	public static <T> Comparator<T> composite(Comparator<T>... comparators) {
		return new CompositeComparator<>(comparators);
	}
}
