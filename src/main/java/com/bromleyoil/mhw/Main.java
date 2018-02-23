package com.bromleyoil.mhw;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main {

	public static void main(String[] args) throws IOException {
		DataParser.parseEquipment(getReader("equipment.tsv"));
	}

	public static Reader getReader(String resourceName) {
		return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
	}
}
