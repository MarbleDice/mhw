package com.bromleyoil.mhw.parser;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SkillParserTest {

	@Test
	public void printEnumInitializers() throws IOException {
		List<String> lines = SkillParser.getEnumInitializerLines();

		assertThat("num lines", lines.size(), is(467));

		lines.clear();
		for (String line : lines) {
			System.out.println(line);
		}
	}
}
