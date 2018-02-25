package com.bromleyoil.mhw.model;

import java.util.Comparator;

public class SkillValue {

	public static final Comparator<SkillValue> POINT_ORDER = (a, b) -> {
		int rv = PointValue.VALUE_ORDER.compare(a.getPointValue(), b.getPointValue());
		if (rv == 0) {
			rv = Skill.NAME_ORDER.compare(a.getSkill(), b.getSkill());
		}
		return rv;
	};

	private Skill skill;
	private PointValue pointValue;

	public SkillValue() {
	}

	public SkillValue(Skill skill, PointValue pointValue) {
		this.skill = skill;
		this.pointValue = pointValue;
	}

	@Override
	public String toString() {
		return skill + " " + pointValue;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public PointValue getPointValue() {
		return pointValue;
	}

	public void setPointValue(PointValue pointValue) {
		this.pointValue = pointValue;
	}
}
