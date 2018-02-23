package com.bromleyoil.mhw;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) throws IOException {
		EquipmentList equipmentList = new EquipmentList(DataParser.parseEquipment(getReader("equipment.tsv")));

		for (Equipment equipment : equipmentList.filter(Skill.EARPLUGS).getItems()) {
			System.out.println(equipment);
		}

		for (Entry<String, Equipment[]> entry : equipmentList.filter(Skill.EARPLUGS).getMatrix().entrySet()) {
			System.out.print(entry.getKey());
			for (Equipment equipment : entry.getValue()) {
				System.out.print(" | " + equipment);
			}
			System.out.println();
		}
	}

	public static Reader getReader(String resourceName) {
		return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
	}
}
