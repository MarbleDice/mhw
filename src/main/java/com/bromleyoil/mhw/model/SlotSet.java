package com.bromleyoil.mhw.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlotSet {

	public static final SlotSet ONE = new SlotSet(1);
	public static final SlotSet ONE_ONE = new SlotSet(1, 1);
	public static final SlotSet ONE_ONE_ONE = new SlotSet(1, 1, 1);
	public static final SlotSet TWO = new SlotSet(2);
	public static final SlotSet TWO_ONE = new SlotSet(2, 1);
	public static final SlotSet THREE = new SlotSet(3);
	public static final SlotSet THREE_ONE = new SlotSet(3, 1);

	public static final Comparator<SlotSet> SUPERIOR_ORDER = (a, b) -> {
		int onePlus = Integer.compare(a.getOnePlus(), b.getOnePlus());
		int twoPlus = Integer.compare(a.getTwoPlus(), b.getTwoPlus());
		int three = Integer.compare(a.getThree(), b.getThree());
		if (onePlus == 0 && twoPlus == 0 && three == 0) {
			return 0;
		} else if (onePlus >= 0 && twoPlus >= 0 && three >= 0) {
			return 1;
		} else if (onePlus <= 0 && twoPlus <= 0 && three <= 0) {
			return -1;
		} else {
			return 0;
		}
	};

	private List<Integer> slots = new ArrayList<>();

	public SlotSet() {
	}

	public SlotSet(int... slots) {
		add(slots);
	}

	public SlotSet(SlotSet slotSet) {
		add(slotSet);
	}

	public void add(int... slots) {
		for (int slot : slots) {
			if (slot < 1 || slot > 3) {
				throw new InvalidSlotException();
			}
			this.slots.add(slot);
		}
		this.slots.sort(Integer::compareTo);
	}

	public void add(SlotSet slotSet) {
		slots.addAll(slotSet.slots);
		slots.sort(Integer::compareTo);
	}

	public List<Integer> getSlots() {
		return slots;
	}

	public static String getSlotLabel(Integer slotLevel) {
		if (slotLevel == 1) {
			return "①";
		} else if (slotLevel == 2) {
			return "②";
		}
		return "③";
	}

	public String getLabel() {
		return slots.stream().map(SlotSet::getSlotLabel).collect(Collectors.joining(" "));
	}

	public void setSlots(List<Integer> slots) {
		this.slots = slots;
	}

	public int getCount() {
		return slots.size();
	}

	public int getOnePlus() {
		return slots.size();
	}

	public int getTwoPlus() {
		return (int) slots.stream().filter(x -> x >= 2).count();
	}

	public int getThree() {
		return (int) slots.stream().filter(x -> x >= 3).count();
	}

	public static class InvalidSlotException extends IllegalArgumentException {

		private static final long serialVersionUID = 1L;

		public InvalidSlotException() {
			super("Slot must be between 1 and 3");
		}
	}
}
