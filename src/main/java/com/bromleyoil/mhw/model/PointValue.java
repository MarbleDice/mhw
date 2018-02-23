package com.bromleyoil.mhw.model;


public enum PointValue {
	SET4(15), SET3(20), SET2(30), ONE(60), TWO(120), THREE(180);
	
	private static final int DIVISOR = 60;

	private int value;

	private PointValue(int value) {
		this.value = value;
	}

	public static PointValue valueOf(int i) {
		for (PointValue pointValue : values()) {
			if (pointValue.value == i * DIVISOR) {
				return pointValue;
			}
		}

		throw new IllegalArgumentException("No value for point value " + i);
	}
}
