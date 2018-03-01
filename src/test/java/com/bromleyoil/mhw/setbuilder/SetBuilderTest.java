package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.Skill.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.SkillSet;

public class SetBuilderTest {

	private static final Logger log = LoggerFactory.getLogger(SetBuilderTest.class);

	private EquipmentList equipmentList;

	@Before
	public void before() throws IOException {
		equipmentList = new EquipmentList();
		equipmentList.postConstruct();
	}

	@Test
	public void search_gs_solutionsFound() {
		SetBuilder builder = new SetBuilder();
		SkillSet requiredSkillSet = new SkillSet(
				Arrays.asList(EARPLUGS, FOCUS, WEAKNESS_EXPLOIT, AGITATOR, ATTACK_BOOST, CRITICAL_EYE, MAXIMUM_MIGHT),
				Arrays.asList(5, 3, 2, 0, 0, 0, 0));

		List<EquipmentSet> solutions = builder.search(equipmentList, requiredSkillSet).getSolutions();
		for (int i = 0; i < 10 && i < solutions.size(); i++) {
			log.info(solutions.get(i).getLongDescription());
		}
	}

	@Ignore
	@Test
	public void search_ig_solutionsFound() {
		SetBuilder builder = new SetBuilder();
		SkillSet requiredSkillSet = new SkillSet(
				Arrays.asList(POWER_PROLONGER, AGITATOR, WEAKNESS_EXPLOIT),
				Arrays.asList(3, 4, 2));

		List<EquipmentSet> solutions = builder.search(equipmentList, requiredSkillSet).getSolutions();
		for (int i = 0; i < 10 && i < solutions.size(); i++) {
			log.info(solutions.get(i).getLongDescription());
		}
	}
}
