package com.bromleyoil.mhw.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

public class SlotSet {

	public static final int MAX_SLOT_LEVEL = 4;

	private static final String OPEN_SLOTS = "①②③④";
	private static final String FILLED_SLOTS = "❶❷❸❹";

	public static final SlotSet NONE = of(0, 0, 0, 0);
	public static final SlotSet ONE = of(1, 0, 0, 0);
	public static final SlotSet ONE_ONE = of(2, 0, 0, 0);
	public static final SlotSet ONE_ONE_ONE = of(3, 0, 0, 0);
	public static final SlotSet TWO = of(0, 1, 0, 0);
	public static final SlotSet TWO_ONE = of(1, 1, 0, 0);
	public static final SlotSet THREE = of(0, 0, 1, 0);
	public static final SlotSet THREE_ONE = of(1, 0, 1, 0);

	private int[] slots = new int[MAX_SLOT_LEVEL];

	private SlotSet(int... slots) {
		this.slots = slots;
	}

	public static SlotSet of(Integer numSlots1, Integer numSlots2, Integer numSlots3, Integer numSlots4) {
		int[] slots = new int[] { Optional.ofNullable(numSlots1).orElse(0),
				Optional.ofNullable(numSlots2).orElse(0), Optional.ofNullable(numSlots3).orElse(0),
				Optional.ofNullable(numSlots4).orElse(0) };
		if (Arrays.stream(slots).sum() > 18) {
			throw new TooManySlotsException();
		}

		return new SlotSet(slots);
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

	@Override
	public boolean equals(Object other) {
		return other instanceof SlotSet && Arrays.equals(slots, ((SlotSet) other).slots);
	}

	@Override
	public int hashCode() {
		return Objects.hash(slots);
	}

	public SlotSet add(SlotSet slotSet) {
		int[] newSlots = Arrays.copyOf(slots, slots.length);
		IntStream.range(0, newSlots.length).forEach(i -> newSlots[i] += slotSet.slots[i]);
		return new SlotSet(newSlots);
	}

	public SlotSet add(int slotLevel) {
		int[] newSlots = Arrays.copyOf(slots, slots.length);
		newSlots[slotLevel - 1] += 1;
		return new SlotSet(newSlots);
	}

	public SlotSet subtract(SlotSet slotSet) {
		int[] newSlots = Arrays.copyOf(slots, slots.length);
		IntStream.range(0, newSlots.length).forEach(i -> newSlots[i] -= slotSet.slots[i]);
		return new SlotSet(newSlots);
	}

	public boolean hasSlot(int slotLevel) {
		return slots[slotLevel - 1] > 0;
	}

	public String getLabel() {
		return getLabel(level -> OPEN_SLOTS.substring(level - 1, level));
	}

	public String getAsciiLabel() {
		return getLabel(level -> String.format("(%d)", level));
	}

	public String getFilledLabel() {
		return getLabel(level -> FILLED_SLOTS.substring(level - 1, level));
	}

	public String getFilledAsciiLabel() {
		return getLabel(level -> String.format("[%d]", level));
	}

	private String getLabel(IntFunction<String> labelMapper) {
		return IntStream.range(0, slots.length)
				.map(i -> slots.length - 1 - i)
				.filter(i -> slots[i] > 0)
				.mapToObj(i -> StringUtils.repeat(labelMapper.apply(i + 1), " ", slots[i]))
				.collect(Collectors.joining(" "));
	}

	/**
	 * Gets the number of slots of the given level.
	 * 
	 * @param slotLevel
	 * @return
	 */
	public int getSlotCount(int slotLevel) {
		return slots[slotLevel - 1];
	}

	/**
	 * Gets the number of slots that can fit a decoration of the given level.
	 * 
	 * @param decorationLevel
	 * @return
	 */
	public int getSlotCountForDeco(int decorationLevel) {
		return IntStream.range(decorationLevel - 1, slots.length).map(i -> slots[i]).sum();
	}

	/**
	 * Gets the minimum slot level that can fit the given decoration level, or zero if none exists.
	 * 
	 * @param decorationlevel
	 * @return
	 */
	public int getMinSlotLevel(int decorationLevel) {
		return IntStream.rangeClosed(decorationLevel, MAX_SLOT_LEVEL).filter(i -> getSlotCount(i) > 0).findFirst()
				.orElse(0);
	}

	public static class InvalidSlotException extends IllegalArgumentException {

		private static final long serialVersionUID = 1L;

		public InvalidSlotException() {
			super("Slot level must be between 1 and " + MAX_SLOT_LEVEL);
		}
	}

	public static class TooManySlotsException extends IllegalArgumentException {

		private static final long serialVersionUID = 1L;

		public TooManySlotsException() {
			super("Slot count must be between 0 and 18 slots");
		}
	}
}
