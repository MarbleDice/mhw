package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.EquipmentType.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;

@RunWith(JUnit4.class)
public class CandidateListTest {

	private static Logger log = LoggerFactory.getLogger(CandidateList.class);

	private EquipmentList equipmentList;
	private CandidateList candidateList;

	@Before
	public void before() throws IOException {
		equipmentList = new EquipmentList();
		equipmentList.postConstruct();
		candidateList = new CandidateList(equipmentList);
	}

	@Test
	public void buildCandidates_noSkills_onlySlotted() {
		candidateList.buildCandidates(null, new SkillSet());

		log.debug(candidateList.toString());

		assertThat("head candidate count", candidateList.getCandidates(HEAD).size(), greaterThan(0));
		assertThat("chest candidate count", candidateList.getCandidates(CHEST).size(), greaterThan(0));
		assertThat("arm candidate count", candidateList.getCandidates(ARM).size(), greaterThan(0));
		assertThat("waist candidate count", candidateList.getCandidates(WAIST).size(), greaterThan(0));
		assertThat("leg candidate count", candidateList.getCandidates(LEG).size(), greaterThan(0));
		assertThat("charm candidate count", candidateList.getCandidates(CHARM).size(), is(0));
	}

	@Test
	public void buildCandidates_greatSword_correct() {
		candidateList.buildCandidates(null, new SkillSet(
				Arrays.asList(Skill.EARPLUGS, Skill.FOCUS, Skill.WEAKNESS_EXPLOIT),
				Arrays.asList(5, 3, 3)));

		log.debug(candidateList.toString());

		assertThat("head candidate count", candidateList.getCandidates(HEAD).size(), greaterThan(2));
		assertThat("chest candidate count", candidateList.getCandidates(CHEST).size(), greaterThan(2));
		assertThat("arm candidate count", candidateList.getCandidates(ARM).size(), greaterThan(2));
		assertThat("waist candidate count", candidateList.getCandidates(WAIST).size(), greaterThan(2));
		assertThat("leg candidate count", candidateList.getCandidates(LEG).size(), greaterThan(2));
		assertThat("charm candidate count", candidateList.getCandidates(CHARM).size(), is(3));
	}
}
