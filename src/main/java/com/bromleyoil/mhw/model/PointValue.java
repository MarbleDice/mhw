package com.bromleyoil.mhw.model;


public enum PointValue {
	SET4(15, "¼"), SET3(20, "⅓"), SET2(30, "½"), ONE(60, "1"), TWO(120, "2"), THREE(180, "3");
	
	private static final int DIVISOR = 60;

	private int value;
	private String label;

	private PointValue(int value, String label) {
		this.value = value;
		this.label = label;
	}

	public static PointValue valueOf(int i) {
		for (PointValue pointValue : values()) {
			if (pointValue.value == i * DIVISOR) {
				return pointValue;
			}
		}

		throw new IllegalArgumentException("No value for point value " + i);
	}

	@Override
	public String toString() {
		return label;
	}
}
