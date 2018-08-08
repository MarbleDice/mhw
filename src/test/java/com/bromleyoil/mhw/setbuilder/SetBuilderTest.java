package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.Skill.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.parser.DataParser;

@RunWith(JUnit4.class)
public class SetBuilderTest {

	private EquipmentList equipmentList;
	private CandidateList candidateList;
	private SetBuilder setBuilder;

	@Before
	public void before() throws IOException {
		equipmentList = new EquipmentList(DataParser.parseAllEquipment());
		candidateList = new CandidateList(equipmentList);
		setBuilder = new SetBuilder(candidateList);
	}

	@Test
	public void search_greatSword_hasSolutions() {
		SkillSet requiredSkillSet = new SkillSet(
				Arrays.asList(EARPLUGS, FOCUS, WEAKNESS_EXPLOIT, AGITATOR),
				Arrays.asList(5, 3, 2, 2));
		setBuilder.setRequiredSkillSet(requiredSkillSet);

		SearchResult result = setBuilder.search();

		assertThat("solutions", result.getSolutions().size(), is(6));
	}

	@Test
	public void search_insectGlaive_hasSolutions() {
		SkillSet requiredSkillSet = new SkillSet(
				Arrays.asList(POWER_PROLONGER, AGITATOR, WEAKNESS_EXPLOIT),
				Arrays.asList(3, 4, 2));
		setBuilder.setRequiredSkillSet(requiredSkillSet);

		SearchResult result = setBuilder.search();

		assertThat("solutions", result.getSolutions().size(), is(60));
	}

	@Test
	public void search_godlyDamageNoDeco_notPossible() {
		SkillSet requiredSkillSet = new SkillSet(
				Arrays.asList(AGITATOR, ATTACK_BOOST, WEAKNESS_EXPLOIT),
				Arrays.asList(5, 7, 3));
		setBuilder.setRequiredSkillSet(requiredSkillSet);

		SearchResult result = setBuilder.search();

		assertThat("solutions", result.getSolutions().size(), is(0));
	}

	@Test
	public void search_godlyDamageWithDeco_hasSolutions() {
		SkillSet requiredSkillSet = new SkillSet(
				Arrays.asList(AGITATOR, ATTACK_BOOST, WEAKNESS_EXPLOIT),
				Arrays.asList(5, 7, 3));
		setBuilder.setRequiredSkillSet(requiredSkillSet);

		Map<Skill, Integer> decorationCounts = new EnumMap<>(Skill.class);
		decorationCounts.put(AGITATOR, 5);
		setBuilder.setDecorationCounts(decorationCounts);

		SearchResult result = setBuilder.search();

		assertThat("solutions", result.getSolutions().size(), is(19));
	}
}
