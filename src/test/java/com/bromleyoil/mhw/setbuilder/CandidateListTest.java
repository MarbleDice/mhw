package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.EquipmentType.*;
import static com.bromleyoil.mhw.model.Skill.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.form.SkillRow;
import com.bromleyoil.mhw.model.EquipmentList;

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
		candidateList.buildCandidates(new SetBuilderForm());

		log.debug(candidateList.toString());

		assertThat("head candidate count", candidateList.getCandidates(HEAD).size(), greaterThan(0));
		assertThat("chest candidate count", candidateList.getCandidates(CHEST).size(), greaterThan(0));
		assertThat("arm candidate count", candidateList.getCandidates(ARM).size(), greaterThan(0));
		assertThat("waist candidate count", candidateList.getCandidates(WAIST).size(), greaterThan(0));
		assertThat("leg candidate count", candidateList.getCandidates(LEG).size(), greaterThan(0));
		assertThat("charm candidate count", candidateList.getCandidates(CHARM).size(), is(1));
	}

	@Test
	public void buildCandidates_greatSword_correct() {
		SetBuilderForm form = new SetBuilderForm();
		form.addSkillRow(new SkillRow(EARPLUGS, 5, 0));
		form.addSkillRow(new SkillRow(FOCUS, 3, 0));
		form.addSkillRow(new SkillRow(WEAKNESS_EXPLOIT, 3, 0));

		candidateList.buildCandidates(form);

		log.debug(candidateList.toString());

		assertThat("head candidate count", candidateList.getCandidates(HEAD).size(), greaterThan(2));
		assertThat("chest candidate count", candidateList.getCandidates(CHEST).size(), greaterThan(2));
		assertThat("arm candidate count", candidateList.getCandidates(ARM).size(), greaterThan(2));
		assertThat("waist candidate count", candidateList.getCandidates(WAIST).size(), greaterThan(2));
		assertThat("leg candidate count", candidateList.getCandidates(LEG).size(), greaterThan(2));
		assertThat("charm candidate count", candidateList.getCandidates(CHARM).size(), is(3));
	}
}
