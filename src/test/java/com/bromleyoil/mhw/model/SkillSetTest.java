package com.bromleyoil.mhw.model;

import static com.bromleyoil.mhw.model.Skill.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SkillSetTest {

	@Test
	public void add_beyondMax_cannotBeImproved() {
		SkillSet base = new SkillSet(SLUGGER, SLUGGER.getMaxLevel());
		base.add(SLUGGER, SLUGGER.getMaxLevel());

		assertThat("skill unchanged", base.getLevel(SLUGGER), is(SLUGGER.getMaxLevel()));
	}

	@Test
	public void subtract_largerSubtrahend_emptyDifference() {
		SkillSet minuend = new SkillSet(SLUGGER, 3);
		SkillSet subtrahend = new SkillSet(SLUGGER, 3);

		SkillSet difference = minuend.subtractByLevel(subtrahend);

		assertThat("difference empty", difference.getSkills(), empty());
	}

	@Test
	public void subtract_smallerSubtrahend_nonZeroDifference() {
		SkillSet minuend = new SkillSet(SLUGGER, 3);
		SkillSet subtrahend = new SkillSet(SLUGGER, 1);

		SkillSet difference = minuend.subtractByLevel(subtrahend);

		assertThat("minuend skill level", minuend.getLevel(SLUGGER), is(3));

		assertThat("difference set size", difference.getSkills().size(), is(1));
		assertThat("difference skill level", difference.getLevel(SLUGGER), is(2));
	}

	@Test
	public void subtract_disjointSubtrahend_noChange() {
		SkillSet minuend = new SkillSet(SLUGGER, 3);
		SkillSet subtrahend = new SkillSet(STAMINA_THIEF, 2);

		SkillSet difference = minuend.subtractByLevel(subtrahend);

		assertThat("difference set size", difference.getSkills().size(), is(1));
		assertThat("difference skill level", difference.getLevel(SLUGGER), is(3));
	}
}
