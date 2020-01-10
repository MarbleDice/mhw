package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.Skill.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.form.SkillRow;
import com.bromleyoil.mhw.model.EquipmentList;

@RunWith(JUnit4.class)
public class NaiveSetBuilderTest {

	private EquipmentList equipmentList;
	private CandidateList candidateList;
	private NaiveSetBuilder setBuilder;

	@Before
	public void before() throws IOException {
		equipmentList = new EquipmentList();
		equipmentList.postConstruct();
		candidateList = new CandidateList(equipmentList);
		setBuilder = new NaiveSetBuilder(equipmentList, candidateList);
	}

	@Test
	public void search_greatSword_hasSolutions() {
		SetBuilderForm form = new SetBuilderForm();
		form.addSkillRow(new SkillRow(EARPLUGS, 5, 0));
		form.addSkillRow(new SkillRow(FOCUS, 3, 0));
		form.addSkillRow(new SkillRow(WEAKNESS_EXPLOIT, 2, 0));
		form.addSkillRow(new SkillRow(AGITATOR, 2, 0));

		SearchResult result = setBuilder.search(form);

		assertThat("solutions", result.getSolutions().size(), greaterThan(0));
	}

	@Test
	public void search_insectGlaive_hasSolutions() {
		SetBuilderForm form = new SetBuilderForm();
		form.addSkillRow(new SkillRow(POWER_PROLONGER, 3, 0));
		form.addSkillRow(new SkillRow(AGITATOR, 4, 0));
		form.addSkillRow(new SkillRow(WEAKNESS_EXPLOIT, 2, 0));

		SearchResult result = setBuilder.search(form);

		assertThat("solutions", result.getSolutions().size(), greaterThan(0));
	}

	@Test
	public void search_godlyDamageNoDeco_notPossible() {
		SetBuilderForm form = new SetBuilderForm();
		form.addSkillRow(new SkillRow(ATTACK_BOOST, 7, 0));
		form.addSkillRow(new SkillRow(CRITICAL_EYE, 7, 0));
		form.addSkillRow(new SkillRow(WEAKNESS_EXPLOIT, 3, 0));

		SearchResult result = setBuilder.search(form);

		assertThat("solutions", result.getSolutions().size(), is(0));
	}

	@Test
	public void search_godlyDamageWithDeco_hasSolutions() {
		SetBuilderForm form = new SetBuilderForm();
		form.addSkillRow(new SkillRow(ATTACK_BOOST, 7, 0));
		form.addSkillRow(new SkillRow(CRITICAL_EYE, 7, 0));
		form.addSkillRow(new SkillRow(WEAKNESS_EXPLOIT, 3, 3));

		SearchResult result = setBuilder.search(form);

		assertThat("solutions", result.getSolutions().size(), greaterThan(0));
	}
}
