package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.Skill.*;
import static com.bromleyoil.mhw.model.SlotSet.*;
import static com.bromleyoil.mhw.setbuilder.Superiority.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;

public class SuperiorityTest {

	private Set<Skill> greatSwordSkills = new HashSet<>(Arrays.asList(EARPLUGS, FOCUS));
	private SkillSet earplugs1 = new SkillSet(EARPLUGS, 1);
	private SkillSet earplugs2 = new SkillSet(EARPLUGS, 2);
	private SkillSet focus1 = new SkillSet(FOCUS, 1);
	private SkillSet marathon1 = new SkillSet(MARATHON_RUNNER, 1);
	private SkillSet guard1 = new SkillSet(GUARD, 1);

	@Test
	public void compare_moreSlots_better() {
		assertThat("1 1 vs 1", compare(ONE_ONE, ONE), is(BETTER));
	}

	@Test
	public void compare_higherSlots_better() {
		assertThat("2 vs 1", compare(TWO, ONE), is(BETTER));
	}

	@Test
	public void compare_moreAndLowerSlots_incomparable() {
		assertThat("2 vs 1 1", compare(TWO, ONE_ONE), is(INCOMPARABLE));
	}

	@Test
	public void compare_sameSlots_equal() {
		assertThat("2 vs 1 1", compare(THREE, THREE), is(EQUAL));
	}

	@Test
	public void compare_differentRequired_incomparable() {
		assertThat("earplugs1 focus1", compare(earplugs1, focus1, greatSwordSkills), is(INCOMPARABLE));
	}

	@Test
	public void compare_differentNonRequired_equal() {
		assertThat("marathon1 guard1", compare(marathon1, guard1, greatSwordSkills), is(EQUAL));
	}

	@Test
	public void compare_higherSkills_better() {
		assertThat("earplugs2 earplugs1", compare(earplugs2, earplugs1, greatSwordSkills), is(BETTER));
	}

	@Test
	public void compare_lowerSkills_worse() {
		assertThat("earplugs1 earplugs2", compare(earplugs1, earplugs2, greatSwordSkills), is(WORSE));
	}
}
