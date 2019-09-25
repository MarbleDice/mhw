package com.bromleyoil.mhw.model;

import static com.bromleyoil.mhw.model.EquipmentType.*;
import static com.bromleyoil.mhw.model.Skill.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.bromleyoil.mhw.parser.DataParser;

@RunWith(JUnit4.class)
public class EquipmentSetTest {

	private EquipmentList equipmentList;

	private EquipmentSet set;

	@Before
	public void before() throws IOException {
		equipmentList = new EquipmentList(DataParser.parseAllEquipment());

		set = new EquipmentSet();
		set.add(equipmentList.find("Damascus β", HEAD));
		set.add(equipmentList.find("Damascus β", CHEST));
		set.add(equipmentList.find("Damascus β", ARM));
		set.add(equipmentList.find("Damascus β", WAIST));
		set.add(equipmentList.find("Damascus β", LEG));
	}

	@Test
	public void decorate_surplusDecorations_requiredNotExceeded() {
		Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);
		decorationCounts.put(ATTACK_BOOST, 7);

		set.decorate(new SkillSet(ATTACK_BOOST, 3), decorationCounts);

		assertThat("decorated level", set.getSkillSet().getLevel(ATTACK_BOOST), is(3));
	}

	@Test
	public void decorate_withWeaponSlot_requiredIsMet() {
		Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);
		decorationCounts.put(EARPLUGS, 3);

		set.setWeaponSlotSet(new SlotSet(1, 0, 0));
		set.decorate(new SkillSet(EARPLUGS, 3), decorationCounts);

		assertThat("decorated level", set.getSkillSet().getLevel(EARPLUGS), is(3));
	}

	@Test
	public void decorate_withoutWeaponSlot_requiredNotMet() {
		Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);
		decorationCounts.put(EARPLUGS, 3);

		set.decorate(new SkillSet(EARPLUGS, 3), decorationCounts);

		assertThat("decorated level", set.getSkillSet().getLevel(EARPLUGS), is(2));
	}

	@Test
	public void decorate_limitedDecorations_requiredNotMet() {
		Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);
		decorationCounts.put(ATTACK_BOOST, 2);

		set.decorate(new SkillSet(ATTACK_BOOST, 4), decorationCounts);

		assertThat("decorated level", set.getSkillSet().getLevel(ATTACK_BOOST), is(0));
	}
}
