package com.bromleyoil.mhw.model;

public enum Fraction {
	SET5(12, "⅕"), SET4(15, "¼"), SET3(20, "⅓"), SET2(30, "½");

	private static final int DENOMINATOR = 60;

	private int numerator;
	private String label;

	private Fraction(int value, String label) {
		this.numerator = value;
		this.label = label;
	}

	public static int getNumerator(int quotient) {
		return DENOMINATOR * quotient;
	}

	public static int getQuotient(int numerator) {
		return numerator / DENOMINATOR;
	}

	public static String getLabel(int numerator) {
		int quotient = numerator / DENOMINATOR;
		return String.format("%s%s", quotient > 0 ? quotient : "", getRemainderLabel(numerator));
	}

	protected static String getRemainderLabel(int numerator) {
		int remainder = numerator % DENOMINATOR;

		if (remainder == 0) {
			return "";
		}

		for (Fraction fraction : values()) {
			if (fraction.numerator == remainder) {
				return fraction.label;
			}
		}

		return String.format(" %d/%d", remainder, DENOMINATOR);
	}

	public int getNumerator() {
		return numerator;
	}

	public int getQuotient() {
		return numerator / DENOMINATOR;
	}

	public String getLabel() {
		return label;
	}
}
