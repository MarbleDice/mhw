package com.bromleyoil.mhw.form;

import com.bromleyoil.mhw.model.Skill;

public class SkillRow {

	private Skill skill;
	private Integer level;
	private Integer decorationCount;

	public SkillRow() {		
	}

	public SkillRow(Skill skill) {
		this.skill = skill;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getDecorationCount() {
		return decorationCount;
	}

	public void setDecorationCount(Integer decorationCount) {
		this.decorationCount = decorationCount;
	}
}
