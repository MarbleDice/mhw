package com.bromleyoil.mhw.form;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.bromleyoil.mhw.model.Skill;

public class SkillFormatter implements Formatter<Skill> {

	@Override
	public String print(Skill skill, Locale arg1) {
		return skill.getUrlName();
	}

	@Override
	public Skill parse(String label, Locale arg1) throws ParseException {
		return Skill.valueOfName(label);
	}
}
