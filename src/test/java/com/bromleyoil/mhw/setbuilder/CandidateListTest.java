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

import com.bromleyoil.mhw.DataParser;
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
		equipmentList = new EquipmentList(DataParser.parseAllEquipment());
		candidateList = new CandidateList(equipmentList);
	}

	@Test
	public void buildCandidates_noSkills_onlySlotted() {
		candidateList.setRequiredSkillSet(new SkillSet());

		log.debug(candidateList.toString());

		assertThat("head candidate count", candidateList.getCandidates(HEAD).size(), is(4));
		assertThat("body candidate count", candidateList.getCandidates(BODY).size(), is(7));
		assertThat("hands candidate count", candidateList.getCandidates(HANDS).size(), is(10));
		assertThat("waist candidate count", candidateList.getCandidates(WAIST).size(), is(10));
		assertThat("legs candidate count", candidateList.getCandidates(LEGS).size(), is(8));
		assertThat("charm candidate count", candidateList.getCandidates(CHARM).size(), is(0));
	}

	@Test
	public void buildCandidates_greatSword_correct() {
		candidateList.setRequiredSkillSet(new SkillSet(
				Arrays.asList(Skill.EARPLUGS, Skill.FOCUS, Skill.WEAKNESS_EXPLOIT),
				Arrays.asList(5, 3, 3)));

		log.debug(candidateList.toString());

		assertThat("head candidate count", candidateList.getCandidates(HEAD).size(), is(6));
		assertThat("body candidate count", candidateList.getCandidates(BODY).size(), is(10));
		assertThat("hands candidate count", candidateList.getCandidates(HANDS).size(), is(6));
		assertThat("waist candidate count", candidateList.getCandidates(WAIST).size(), is(6));
		assertThat("legs candidate count", candidateList.getCandidates(LEGS).size(), is(12));
		assertThat("charm candidate count", candidateList.getCandidates(CHARM).size(), is(3));
	}
}
