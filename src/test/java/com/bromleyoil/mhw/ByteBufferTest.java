package com.bromleyoil.mhw;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ByteBufferTest {

	@Test
	public void writeToBytes_smallValue_oneByte() {
		ByteBuffer buffer = new ByteBuffer(2);

		buffer.write(127, 1);

		assertThat("byte 0", buffer.getBytes()[0], is((byte) 127));
		assertThat("byte 1", buffer.getBytes()[1], is((byte) 0));
	}

	@Test
	public void writeToBytes_smallValueWithLength_isPadded() {
		ByteBuffer buffer = new ByteBuffer(2);

		buffer.write(59, 2);

		assertThat("byte 0", buffer.getBytes()[0], is((byte) 0));
		assertThat("byte 1", buffer.getBytes()[1], is((byte) 59));
	}

	@Test(expected = IllegalArgumentException.class)
	public void writeToBytes_valueTooLarge_throws() {
		ByteBuffer buffer = new ByteBuffer(2);

		buffer.write(64000, 1);
	}
}
