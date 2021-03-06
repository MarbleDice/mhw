package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.EquipmentType.*;
import static com.bromleyoil.mhw.model.Skill.*;
import static com.bromleyoil.mhw.model.SlotSet.*;
import static com.bromleyoil.mhw.setbuilder.Superiority.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.SetBonus;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;

@RunWith(JUnit4.class)
public class SuperiorityTest {

	private Set<Skill> greatSwordSkills = new HashSet<>(Arrays.asList(EARPLUGS, FOCUS));
	private SkillSet earplugs1 = new SkillSet(EARPLUGS, 1);
	private SkillSet earplugs2 = new SkillSet(EARPLUGS, 2);
	private SkillSet focus1 = new SkillSet(FOCUS, 1);
	private SkillSet marathon1 = new SkillSet(MARATHON_RUNNER, 1);
	private SkillSet guard1 = new SkillSet(GUARD, 1);
	private Equipment eqBetterHead = mockEquip(HEAD, earplugs2, SlotSet.NONE, SetBonus.NONE);
	private Equipment eqHead = mockEquip(HEAD, earplugs1, SlotSet.NONE, SetBonus.NONE);
	private Equipment eqChest = mockEquip(CHEST, earplugs1, SlotSet.NONE, SetBonus.NONE);

	@Test
	public void compare_sameEquip_equal() {
		assertThat("head vs head", compare(eqHead, eqHead, greatSwordSkills), is(EQUAL));
	}

	@Test
	public void compare_betterSkills_better() {
		assertThat("head vs chest", compare(eqBetterHead, eqHead, greatSwordSkills), is(BETTER));
	}

	@Test
	public void compare_differentTypes_incomparable() {
		assertThat("head vs chest", compare(eqBetterHead, eqChest, greatSwordSkills), is(INCOMPARABLE));
	}

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
		assertThat("3 vs 3", compare(THREE, THREE), is(EQUAL));
	}

	@Test
	public void equalOrBetter_moreSlots_true() {
		assertThat("1 1 vs 1", equalOrBetter(ONE_ONE, ONE), is(true));
	}

	@Test
	public void equalOrBetter_higherSlots_true() {
		assertThat("2 vs 1", equalOrBetter(TWO, ONE), is(true));
	}

	@Test
	public void equalOrBetter_moreAndLowerSlots_false() {
		assertThat("2 vs 1 1", equalOrBetter(TWO, ONE_ONE), is(false));
	}

	@Test
	public void equalOrBetter_sameSlots_true() {
		assertThat("3 vs 3", equalOrBetter(THREE, THREE), is(true));
	}

	@Test
	public void equalOrBetter_missingSkill_false() {
		assertThat("earplugs1 < focus1", equalOrBetter(earplugs1, focus1), is(false));
	}

	@Test
	public void equalOrBetter_lowSkill_false() {
		assertThat("earplugs1 < earplugs2", equalOrBetter(earplugs1, earplugs2), is(false));
	}

	@Test
	public void equalOrBetter_equalSkills_true() {
		assertThat("guard1 = guard1", equalOrBetter(guard1, guard1), is(true));
	}

	@Test
	public void compare_differentRequired_incomparable() {
		assertThat("earplugs1 focus1", compare(earplugs1, focus1, greatSwordSkills), is(INCOMPARABLE));
	}

	@Test
	public void compare_differentNonRequired_equal() {
		assertThat("marathon1 guard1", compare(marathon1, guard1, greatSwordSkills), is(EQUAL));
	}

	/**
	 * Candidates are somewhat permissive, "equal" entries according to interesting skills are allowed in for more
	 * variety, however in this example one really is superior and could be filtered out of the candidate list. Sets
	 * with the inferior piece will get filtered out of solutions.
	 */
	@Test
	@Ignore
	public void compare_higherUninterestingSkills_better() {
		Equipment e1 = new Equipment();
		e1.setSlotSet(SlotSet.THREE);
		e1.setSkillSet(new SkillSet(WINDPROOF, 2));

		Equipment e2 = new Equipment();
		e2.setSlotSet(SlotSet.THREE);
		e2.setSkillSet(new SkillSet(WINDPROOF, 1));

		assertThat("Rath Soul β vs Ingot β", compare(e1, e2, greatSwordSkills), is(BETTER));
	}

	@Test
	public void compare_higherSkills_better() {
		assertThat("earplugs2 earplugs1", compare(earplugs2, earplugs1, greatSwordSkills), is(BETTER));
	}

	@Test
	public void compare_lowerSkills_worse() {
		assertThat("earplugs1 earplugs2", compare(earplugs1, earplugs2, greatSwordSkills), is(WORSE));
	}

	private Equipment mockEquip(EquipmentType type, SkillSet skillSet, SlotSet slotSet, SetBonus setBonus) {
		Equipment equip = new Equipment();
		equip.setType(type);
		equip.setSkillSet(skillSet);
		equip.setSlotSet(slotSet);
		equip.setSetBonus(setBonus);
		return equip;
	}
}
