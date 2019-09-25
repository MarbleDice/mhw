package com.bromleyoil.mhw.model;

public enum EquipmentType {
	HEAD("Head"), CHEST("Chest"), ARM("Arm"), WAIST("Waist"), LEG("Leg"), CHARM("Charm");

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
