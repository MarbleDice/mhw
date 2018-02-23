package com.bromleyoil.mhw;

public enum EquipmentType {
	HEAD("Head"), BODY("Body"), HANDS("Hands"), WAIST("Waist"), LEGS("Legs"), CHARM("Charm");

	private String description;

	private EquipmentType(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return getDescription();
	}

	public String getDescription() {
		return description;
	}
}
