package com.bromleyoil.mhw.setbuilder;

import static com.bromleyoil.mhw.model.Skill.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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
	public void search_ig_solutionsFound() {
		SetBuilder builder = new SetBuilder();
		SkillSet requiredSkillSet = new SkillSet(
				Arrays.asList(POWER_PROLONGER, AGITATOR, WEAKNESS_EXPLOIT, ATTACK_BOOST),
				Arrays.asList(3, 3, 2, 2));

		List<EquipmentSet> solutions = builder.search(equipmentList, requiredSkillSet);
		for (int i = 0; i < 10 && i < solutions.size(); i++) {
			log.info(solutions.get(i).toString());
		}
	}
}
