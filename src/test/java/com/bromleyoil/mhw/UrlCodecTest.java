package com.bromleyoil.mhw;

import static com.bromleyoil.mhw.model.EquipmentType.*;
import static com.bromleyoil.mhw.model.Skill.*;
import static com.bromleyoil.mhw.setbuilder.Superiority.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.parser.DataParser;

@RunWith(JUnit4.class)
public class UrlCodecTest {

	private EquipmentList equipmentList;

	@Before
	public void before() throws IOException {
		equipmentList = new EquipmentList(DataParser.parseAllEquipment());
	}

	@Test
	public void girrosSet_noDeco_decodedEqual() {
		EquipmentSet set = new EquipmentSet();
		set.add(equipmentList.find("Girros α", HEAD));
		set.add(equipmentList.find("Girros α", BODY));
		set.add(equipmentList.find("Girros α", HANDS));
		set.add(equipmentList.find("Girros α", WAIST));
		set.add(equipmentList.find("Girros α", LEGS));
		set.add(equipmentList.find("Attack Charm I"));

		EquipmentSet decodedSet = UrlCodec.decode(equipmentList, set.getBase64());

		assertThat("equal", compare(set, decodedSet), is(EQUAL));
	}

	@Test
	public void nergiSet_withDeco_decodedEqual() {
		EquipmentSet set = new EquipmentSet();
		set.add(equipmentList.find("Nergigante α", HEAD));
		set.add(equipmentList.find("Nergigante β", BODY));
		set.add(equipmentList.find("Nergigante α", HANDS));
		set.add(equipmentList.find("Nergigante α", WAIST));
		set.add(equipmentList.find("Nergigante α", LEGS));
		set.add(equipmentList.find("Challenger Charm II"));
		set.decorate(ATTACK_BOOST, 3);

		EquipmentSet decodedSet = UrlCodec.decode(equipmentList, set.getBase64());

		assertThat("equal", compare(set, decodedSet), is(EQUAL));
	}

	@Test
	public void allDamage_withTwoDeco_decodedEqual() {
		EquipmentSet set = new EquipmentSet();
		set.add(equipmentList.find("Dragonking α", HEAD));
		set.add(equipmentList.find("Dober β", BODY));
		set.add(equipmentList.find("Nergigante α", HANDS));
		set.add(equipmentList.find("Vaal Hazak β", WAIST));
		set.add(equipmentList.find("Dober β", LEGS));
		set.add(equipmentList.find("Unscathed Charm II"));
		set.decorate(ATTACK_BOOST, 1);
		set.decorate(WEAKNESS_EXPLOIT, 1);

		EquipmentSet decodedSet = UrlCodec.decode(equipmentList, set.getBase64());

		assertThat("equal", compare(set, decodedSet), is(EQUAL));
	}

}
