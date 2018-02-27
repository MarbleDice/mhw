package com.bromleyoil.mhw;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentType;

@RunWith(SpringRunner.class)
public class DataParserTest {

	@Test
	public void parseSkills_fullFile_parsed() throws IOException {
		List<String> lines = DataParser.parseSkills(DataParser.openResource("skills.tsv"));
		assertThat("num skill lines", lines.size(), is(467));

		lines.clear();
		for (String line : lines) {
			System.out.println(line);
		}
	}

	@Test
	public void createEquipment_withSlots_parsed() {
		Equipment equipment = DataParser.createEquip(mockRecord("test beta"), EquipmentType.HEAD, "Handicraft 2 (2)");
		assertThat("num slots", equipment.getSlots().getCount(), is(1));
	}

	@Test
	public void addSlots_threeSlot_parsed() {
		Equipment equipment = new Equipment();
		DataParser.addSlots(equipment, "(3,1)");
		assertThat("num slots", equipment.getSlots().getCount(), is(2));
	}

	@Test
	public void parseCharms_fullFile_parsed() throws IOException {
		List<Equipment> charms = DataParser.parseCharms(DataParser.openResource("charms.tsv"));

		assertThat("num charms", charms.size(), is(228));
	}

	public CSVRecord mockRecord(String name, String... setBonuses) {
		if (setBonuses.length < 3) {
			String[] paddedBonuses = new String[] { "", "", "" };
			for (int i = 0; i < setBonuses.length; i++) {
				paddedBonuses[i] = setBonuses[i] == null ? "" : setBonuses[i];
			}
			setBonuses = paddedBonuses;
		}
		String content = "Name\tHead\tBody\tHands\tWaist\tLegs\tSet2\tSet3\tSet4\n"
				+ name + "\t\t\t\t\t\t" + String.join("\t", setBonuses);
		CSVRecord record;
		try {
			record = CSVFormat.TDF.withFirstRecordAsHeader().parse(new StringReader(content)).getRecords().get(0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return record;
	}
}
