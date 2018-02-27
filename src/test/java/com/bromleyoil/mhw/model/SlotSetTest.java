package com.bromleyoil.mhw.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SlotSetTest {

	@Test
	public void compare_zero_alwaysLess() {
		assertThat("0 v   0", SlotSet.SUPERIORITY.compare(SlotSet.ZERO, SlotSet.ZERO), is(0));
		assertThat("0 v   1", SlotSet.SUPERIORITY.compare(SlotSet.ZERO, SlotSet.ONE), lessThan(0));
		assertThat("0 v  11", SlotSet.SUPERIORITY.compare(SlotSet.ZERO, SlotSet.ONE_ONE), lessThan(0));
		assertThat("0 v 111", SlotSet.SUPERIORITY.compare(SlotSet.ZERO, SlotSet.ONE_ONE_ONE), lessThan(0));
		assertThat("0 v   2", SlotSet.SUPERIORITY.compare(SlotSet.ZERO, SlotSet.TWO), lessThan(0));
		assertThat("0 v  21", SlotSet.SUPERIORITY.compare(SlotSet.ZERO, SlotSet.TWO_ONE), lessThan(0));
		assertThat("0 v   3", SlotSet.SUPERIORITY.compare(SlotSet.ZERO, SlotSet.THREE), lessThan(0));
		assertThat("0 v  31", SlotSet.SUPERIORITY.compare(SlotSet.ZERO, SlotSet.THREE_ONE), lessThan(0));
	}

	@Test
	public void compare_one_mostlyLess() {
		assertThat("1 v   0", SlotSet.SUPERIORITY.compare(SlotSet.ONE, SlotSet.ZERO), greaterThan(0));
		assertThat("1 v   1", SlotSet.SUPERIORITY.compare(SlotSet.ONE, SlotSet.ONE), is(0));
		assertThat("1 v  11", SlotSet.SUPERIORITY.compare(SlotSet.ONE, SlotSet.ONE_ONE), lessThan(0));
		assertThat("1 v 111", SlotSet.SUPERIORITY.compare(SlotSet.ONE, SlotSet.ONE_ONE_ONE), lessThan(0));
		assertThat("1  v  2", SlotSet.SUPERIORITY.compare(SlotSet.ONE, SlotSet.TWO), lessThan(0));
		assertThat("1 v  21", SlotSet.SUPERIORITY.compare(SlotSet.ONE, SlotSet.TWO_ONE), lessThan(0));
		assertThat("1 v   3", SlotSet.SUPERIORITY.compare(SlotSet.ONE, SlotSet.THREE), lessThan(0));
		assertThat("1 v  31", SlotSet.SUPERIORITY.compare(SlotSet.ONE, SlotSet.THREE_ONE), lessThan(0));
	}

	@Test
	public void compare_oneOne_somewhatLess() {
		assertThat("11 v   0", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE, SlotSet.ZERO), greaterThan(0));
		assertThat("11 v   1", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE, SlotSet.ONE), greaterThan(0));
		assertThat("11 v  11", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE, SlotSet.ONE_ONE), is(0));
		assertThat("11 v 111", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE, SlotSet.ONE_ONE_ONE), lessThan(0));
		assertThat("11 v   2", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE, SlotSet.TWO), is(0));
		assertThat("11 v  21", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE, SlotSet.TWO_ONE), lessThan(0));
		assertThat("11 v   3", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE, SlotSet.THREE), is(0));
		assertThat("11 v  31", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE, SlotSet.THREE_ONE), lessThan(0));
	}

	@Test
	public void compare_oneOneOne_somewhatGreater() {
		assertThat("111 v   0", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE_ONE, SlotSet.ZERO), greaterThan(0));
		assertThat("111 v   1", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE_ONE, SlotSet.ONE), greaterThan(0));
		assertThat("111 v  11", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE_ONE, SlotSet.ONE_ONE), greaterThan(0));
		assertThat("111 v 111", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE_ONE, SlotSet.ONE_ONE_ONE), is(0));
		assertThat("111 v   2", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE_ONE, SlotSet.TWO), is(0));
		assertThat("111 v  21", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE_ONE, SlotSet.TWO_ONE), is(0));
		assertThat("111 v   3", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE_ONE, SlotSet.THREE), is(0));
		assertThat("111 v  31", SlotSet.SUPERIORITY.compare(SlotSet.ONE_ONE_ONE, SlotSet.THREE_ONE), is(0));
	}

	@Test
	public void compare_two_somewhatLess() {
		assertThat("2 v   0", SlotSet.SUPERIORITY.compare(SlotSet.TWO, SlotSet.ZERO), greaterThan(0));
		assertThat("2 v   1", SlotSet.SUPERIORITY.compare(SlotSet.TWO, SlotSet.ONE), greaterThan(0));
		assertThat("2 v  11", SlotSet.SUPERIORITY.compare(SlotSet.TWO, SlotSet.ONE_ONE), is(0));
		assertThat("2 v 111", SlotSet.SUPERIORITY.compare(SlotSet.TWO, SlotSet.ONE_ONE_ONE), is(0));
		assertThat("2 v   2", SlotSet.SUPERIORITY.compare(SlotSet.TWO, SlotSet.TWO), is(0));
		assertThat("2 v  21", SlotSet.SUPERIORITY.compare(SlotSet.TWO, SlotSet.TWO_ONE), lessThan(0));
		assertThat("2 v   3", SlotSet.SUPERIORITY.compare(SlotSet.TWO, SlotSet.THREE), lessThan(0));
		assertThat("2 v  31", SlotSet.SUPERIORITY.compare(SlotSet.TWO, SlotSet.THREE_ONE), lessThan(0));
	}

	@Test
	public void compare_twoOne_mostlyGreater() {
		assertThat("21 v   0", SlotSet.SUPERIORITY.compare(SlotSet.TWO_ONE, SlotSet.ZERO), greaterThan(0));
		assertThat("21 v   1", SlotSet.SUPERIORITY.compare(SlotSet.TWO_ONE, SlotSet.ONE), greaterThan(0));
		assertThat("21 v  11", SlotSet.SUPERIORITY.compare(SlotSet.TWO_ONE, SlotSet.ONE_ONE), greaterThan(0));
		assertThat("21 v 111", SlotSet.SUPERIORITY.compare(SlotSet.TWO_ONE, SlotSet.ONE_ONE_ONE), is(0));
		assertThat("21 v   2", SlotSet.SUPERIORITY.compare(SlotSet.TWO_ONE, SlotSet.TWO), greaterThan(0));
		assertThat("21 v  21", SlotSet.SUPERIORITY.compare(SlotSet.TWO_ONE, SlotSet.TWO_ONE), is(0));
		assertThat("21 v   3", SlotSet.SUPERIORITY.compare(SlotSet.TWO_ONE, SlotSet.THREE), is(0));
		assertThat("21 v  31", SlotSet.SUPERIORITY.compare(SlotSet.TWO_ONE, SlotSet.THREE_ONE), lessThan(0));
	}

	@Test
	public void compare_three_mostlyGreater() {
		assertThat("3 v   0", SlotSet.SUPERIORITY.compare(SlotSet.THREE, SlotSet.ZERO), greaterThan(0));
		assertThat("3 v   1", SlotSet.SUPERIORITY.compare(SlotSet.THREE, SlotSet.ONE), greaterThan(0));
		assertThat("3 v  11", SlotSet.SUPERIORITY.compare(SlotSet.THREE, SlotSet.ONE_ONE), is(0));
		assertThat("3 v 111", SlotSet.SUPERIORITY.compare(SlotSet.THREE, SlotSet.ONE_ONE_ONE), is(0));
		assertThat("3 v   2", SlotSet.SUPERIORITY.compare(SlotSet.THREE, SlotSet.TWO), greaterThan(0));
		assertThat("3 v  21", SlotSet.SUPERIORITY.compare(SlotSet.THREE, SlotSet.TWO_ONE), is(0));
		assertThat("3 v   3", SlotSet.SUPERIORITY.compare(SlotSet.THREE, SlotSet.THREE), is(0));
		assertThat("3 v  31", SlotSet.SUPERIORITY.compare(SlotSet.THREE, SlotSet.THREE_ONE), lessThan(0));
	}

	@Test
	public void compare_threeOne_alwaysGreater() {
		assertThat("31 v   0", SlotSet.SUPERIORITY.compare(SlotSet.THREE_ONE, SlotSet.ZERO), greaterThan(0));
		assertThat("31 v   1", SlotSet.SUPERIORITY.compare(SlotSet.THREE_ONE, SlotSet.ONE), greaterThan(0));
		assertThat("31 v  11", SlotSet.SUPERIORITY.compare(SlotSet.THREE_ONE, SlotSet.ONE_ONE), greaterThan(0));
		assertThat("31 v 111", SlotSet.SUPERIORITY.compare(SlotSet.THREE_ONE, SlotSet.ONE_ONE_ONE), is(0));
		assertThat("31 v   2", SlotSet.SUPERIORITY.compare(SlotSet.THREE_ONE, SlotSet.TWO), greaterThan(0));
		assertThat("31 v  21", SlotSet.SUPERIORITY.compare(SlotSet.THREE_ONE, SlotSet.TWO_ONE), greaterThan(0));
		assertThat("31 v   3", SlotSet.SUPERIORITY.compare(SlotSet.THREE_ONE, SlotSet.THREE), greaterThan(0));
		assertThat("31 v  31", SlotSet.SUPERIORITY.compare(SlotSet.THREE_ONE, SlotSet.THREE_ONE), is(0));
	}
}
