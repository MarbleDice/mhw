package com.bromleyoil.mhw.model;

public enum Fraction {
	SET4(15, "¼"), SET3(20, "⅓"), SET2(30, "½");

	private static final int DENOMINATOR = 60;

	private int value;
	private String label;

	private Fraction(int value, String label) {
		this.value = value;
		this.label = label;
	}

	public static int getNumerator(int level) {
		return DENOMINATOR * level;
	}

	public static int getLevel(int numerator) {
		return numerator / DENOMINATOR;
	}

	public static String getLabel(int numerator) {
		int quotient = numerator / DENOMINATOR;
		return String.format("%s%s", quotient > 0 ? quotient : "", getFractionalLabel(numerator));
	}

	protected static String getFractionalLabel(int numerator) {
		int remainder = numerator % DENOMINATOR;

		if (remainder == 0) {
			return "";
		}

		for (Fraction fraction : values()) {
			if (fraction.value == remainder) {
				return fraction.label;
			}
		}

		return String.format(" %d/%d", remainder, DENOMINATOR);
	}

	public int getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}
}
