package com.bromleyoil.mhw.setbuilder;

import com.bromleyoil.mhw.model.SlotSet;

public enum Superiority {
	WORSE, EQUAL, BETTER, INCOMPARABLE;

	public static Superiority compare(SlotSet a, SlotSet b) {
		int onePlus = Integer.compare(a.getOnePlus(), b.getOnePlus());
		int twoPlus = Integer.compare(a.getTwoPlus(), b.getTwoPlus());
		int three = Integer.compare(a.getThree(), b.getThree());
		if (onePlus == 0 && twoPlus == 0 && three == 0) {
			return EQUAL;
		} else if (onePlus >= 0 && twoPlus >= 0 && three >= 0) {
			return BETTER;
		} else if (onePlus <= 0 && twoPlus <= 0 && three <= 0) {
			return WORSE;
		}
		return Superiority.INCOMPARABLE;
	}
}
