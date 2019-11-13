package com.bromleyoil.mhw.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JUnit4.class)
public class SlotSetTest {

	Logger log = LoggerFactory.getLogger(SlotSetTest.class);

	@Test
	public void getMinSlotLevel_levelOneWithSlotsOneTwoThree_returnsOne() {
		assertThat("min slot level 1 in (3 2 1)", SlotSet.of(1, 1, 1, 0).getMinSlotLevel(1), is(1));
	}

	@Test
	public void getMinSlotLevel_levelOneWithSlotsTwoThree_returnsTwo() {
		assertThat("min slot level 1 in (3 2)", SlotSet.of(0, 1, 1, 0).getMinSlotLevel(1), is(2));
	}

	@Test
	public void getMinSlotLevel_levelTwoWithSlotsOneFour_returnsFour() {
		assertThat("min slot level 2 in (4 1)", SlotSet.of(1, 0, 0, 1).getMinSlotLevel(2), is(4));
	}

	@Test
	public void getSlotCount_levelThreeWithSlotsOneTwoThree_returnsOne() {
		assertThat("slot count level 3 in (3 2 1)", SlotSet.of(1, 1, 1, 0).getSlotCount(3), is(1));
	}

	@Test
	public void getSlotCountForDeco_levelOneWithSlotsOneTwoThree_returnsTwo() {
		assertThat("slot count for deco level 1 in (3 2 1)", SlotSet.of(1, 1, 1, 0).getSlotCountForDeco(1), is(3));
	}
}
