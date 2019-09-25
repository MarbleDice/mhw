package com.bromleyoil.mhw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.bromleyoil.mhw.comparator.Comparators;

public class SlotSet {

	public static final SlotSet ZERO = new SlotSet();
	public static final SlotSet ONE = new SlotSet(0, 0, 1);
	public static final SlotSet ONE_ONE = new SlotSet(0, 0, 2);
	public static final SlotSet ONE_ONE_ONE = new SlotSet(0, 0, 3);
	public static final SlotSet TWO = new SlotSet(0, 1, 0);
	public static final SlotSet TWO_ONE = new SlotSet(0, 1, 1);
	public static final SlotSet THREE = new SlotSet(1, 0, 0);
	public static final SlotSet THREE_ONE = new SlotSet(1, 0, 1);

	private List<Integer> slots = new ArrayList<>();
	private List<Integer> filledSlots = new ArrayList<>();
	private Map<Skill, Integer> decorationCounts = new TreeMap<>(Skill.DECORATION_AND_NAME_ORDER);

	public SlotSet() {
	}

	public SlotSet(SlotSet slotSet) {
		add(slotSet);
	}

	public SlotSet(Integer numSlots3, Integer numSlots2, Integer numSlots1) {
		add(1, numSlots1);
		add(2, numSlots2);
		add(3, numSlots3);
	}

	public void add(int slotLevel, Integer slotCount) {
		for (int i = 0; slotCount != null && i < slotCount; i++) {
			add(slotLevel);
		}
	}

	public void add(int... slots) {
		for (int slot : slots) {
			// TODO treat as level 3 for now
			slot = slot == 4 ? 3 : slot;

			if (slot < 1 || slot > 3) {
				throw new InvalidSlotException();
			}
			this.slots.add(slot);
		}
		this.slots.sort(Comparators.DESCENDING);
	}

	public void add(SlotSet slotSet) {
		slots.addAll(slotSet.slots);
		slots.sort(Comparators.DESCENDING);
	}

	public boolean decorate(Skill skill) {
		for (int level = skill.getDecorationLevel(); level <= 3; level++) {
			if (slots.contains(level)) {
				// Remove by reference (not index)
				slots.remove(Integer.valueOf(level));
				filledSlots.add(level);
				filledSlots.sort(Comparators.DESCENDING);
				decorationCounts.compute(skill, (k, v) -> v != null ? v + 1 : 1);
				return true;
			}
		}
		return false;
	}

	public boolean hasSlots() {
		return !slots.isEmpty() || !filledSlots.isEmpty();
	}

	public boolean hasOpenSlot(int level) {
		for (int l = level; l <= 3; l++) {
			if (slots.contains(l)) {
				return true;
			}
		}
		return false;
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

	public static String getFilledSlotLabel(Integer slotLevel) {
		if (slotLevel == 1) {
			return "❶";
		} else if (slotLevel == 2) {
			return "❷";
		}
		return "❸";
	}

	public String getLabel() {
		return String.join(" ", filledSlots.stream().map(SlotSet::getFilledSlotLabel).collect(Collectors.joining(" ")),
				slots.stream().map(SlotSet::getSlotLabel).collect(Collectors.joining(" ")));
	}

	public String getAsciiLabel() {
		return String.join(" ",
				filledSlots.stream().map(x -> String.format("[%d]", x)).collect(Collectors.joining(" ")),
				slots.stream().map(x -> String.format("(%d)", x)).collect(Collectors.joining(" ")));
	}

	public boolean isEmpty() {
		return slots.isEmpty();
	}

	public void setSlots(List<Integer> slots) {
		this.slots = slots;
	}

	public int getCount() {
		return slots.size();
	}

	public int getOne() {
		return (int) slots.stream().filter(x -> x == 1).count();
	}

	public int getOnePlus() {
		return slots.size();
	}

	public int getTwo() {
		return (int) slots.stream().filter(x -> x == 2).count();
	}

	public int getTwoPlus() {
		return (int) slots.stream().filter(x -> x >= 2).count();
	}

	public int getThree() {
		return (int) slots.stream().filter(x -> x == 3).count();
	}

	public static class InvalidSlotException extends IllegalArgumentException {

		private static final long serialVersionUID = 1L;

		public InvalidSlotException() {
			super("Slot must be between 1 and 3");
		}
	}
}
