package com.bromleyoil.mhw.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class SlotSet {

	private static final Map<String, SlotSet> SLOT_SETS = new HashMap<>();
	private static final int MAX_SLOT_LEVEL = 4;

	public static final SlotSet NONE = of(0, 0, 0, 0);
	public static final SlotSet ONE = of(1, 0, 0, 0);
	public static final SlotSet ONE_ONE = of(2, 0, 0, 0);
	public static final SlotSet ONE_ONE_ONE = of(3, 0, 0, 0);
	public static final SlotSet TWO = of(0, 1, 0, 0);
	public static final SlotSet TWO_ONE = of(1, 1, 0, 0);
	public static final SlotSet THREE = of(0, 0, 1, 0);
	public static final SlotSet THREE_ONE = of(1, 0, 1, 0);

	private int[] slots = new int[MAX_SLOT_LEVEL];
	private int[] filledSlots = new int[MAX_SLOT_LEVEL];

	private SlotSet(int... slotArray) {
		slots = slotArray;
	}

	private static String hash(int... slotArray) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < slotArray.length; i++) {
			if (i > 0) {
				sb.append("-");
			}
			sb.append(slotArray[i]);
		}
		return sb.toString();
	}

	public static SlotSet of(Integer numSlots1, Integer numSlots2, Integer numSlots3, Integer numSlots4) {
		int[] slotArray = new int[] { Optional.ofNullable(numSlots1).orElse(0),
				Optional.ofNullable(numSlots2).orElse(0), Optional.ofNullable(numSlots3).orElse(0),
				Optional.ofNullable(numSlots4).orElse(0) };
		if (Arrays.stream(slotArray).sum() > 18) {
			throw new TooManySlotsException();
		}

		return SLOT_SETS.computeIfAbsent(hash(slotArray), s -> new SlotSet(slotArray));
	}

	public static SlotSet of(String slotString) {
		Map<Integer, Long> countBySlot = Optional.ofNullable(slotString).orElse("").replaceAll("[^0-9]", "")
				.codePoints()
				.mapToObj(i -> (char) i)
				.map(String::valueOf)
				.map(Integer::parseInt)
				.collect(Collectors.groupingBy(k -> k, Collectors.counting()));

		return of(countBySlot.getOrDefault(1, 0L).intValue(), countBySlot.getOrDefault(2, 0L).intValue(),
				countBySlot.getOrDefault(3, 0L).intValue(), countBySlot.getOrDefault(4, 0L).intValue());
	}

	public SlotSet add(SlotSet slotSet) {
		return SlotSet.of(slots[0] + slotSet.slots[0], slots[1] + slotSet.slots[1],
				slots[2] + slotSet.slots[2], slots[3] + slotSet.slots[3]);
	}

	public boolean decorate(Skill skill) {
		// for (int level = skill.getDecorationLevel(); level <= MAX_SLOT_LEVEL; level++) {
		// if (slots.contains(level)) {
		// // Remove by reference (not index)
		// slots.remove(Integer.valueOf(level));
		// filledSlots.add(level);
		// filledSlots.sort(Comparators.DESCENDING);
		// decorationCounts.compute(skill, (k, v) -> v != null ? v + 1 : 1);
		// return true;
		// }
		// }
		return false;
	}

	public boolean hasSlots() {
		return getOnePlus() > 0;
	}

	public boolean hasOpenSlot(int level) {
		return slots[level - 1] - filledSlots[level - 1] > 0;
	}

	private static String getSlotLabel(Integer slotLevel) {
		if (slotLevel == 1) {
			return "①";
		} else if (slotLevel == 2) {
			return "②";
		} else if (slotLevel == 3) {
			return "③";
		}
		return "④";
	}

	private static String getFilledSlotLabel(Integer slotLevel) {
		if (slotLevel == 1) {
			return "❶";
		} else if (slotLevel == 2) {
			return "❷";
		} else if (slotLevel == 3) {
			return "❸";
		}
		return "❹";
	}

	public String getLabel() {
		return getLabel(filledSlots, SlotSet::getFilledSlotLabel)
				+ " " + getLabel(slots, SlotSet::getSlotLabel);
	}

	public String getAsciiLabel() {
		return getLabel(filledSlots, i -> String.format("[%d]", i))
				+ " " + getLabel(slots, i -> String.format("(%d)", i));
	}

	private static String getLabel(int[] slots, Function<Integer, String> labelMapper) {
		StringBuilder sb = new StringBuilder();
		for (int i = slots.length; i > 0; i--) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(StringUtils.repeat(labelMapper.apply(i), " ", slots[i - 1]));
		}
		return sb.toString();
	}

	public int getOne() {
		return slots[0];
	}

	public int getOnePlus() {
		return slots[0] + slots[1] + slots[2] + slots[3];
	}

	public int getTwo() {
		return slots[1];
	}

	public int getTwoPlus() {
		return slots[1] + slots[2] + slots[3];
	}

	public int getThree() {
		return slots[2];
	}

	public int getThreePlus() {
		return slots[2] + slots[3];
	}

	public int getFour() {
		return slots[3];
	}

	public static class InvalidSlotException extends IllegalArgumentException {

		private static final long serialVersionUID = 1L;

		public InvalidSlotException() {
			super("Slot level must be between 1 and 4");
		}
	}

	public static class TooManySlotsException extends IllegalArgumentException {

		private static final long serialVersionUID = 1L;

		public TooManySlotsException() {
			super("Slot count must be between 0 and 18 slots");
		}
	}
}
