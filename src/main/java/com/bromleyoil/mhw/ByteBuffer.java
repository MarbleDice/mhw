package com.bromleyoil.mhw;

import java.math.BigInteger;
import java.util.Arrays;

public class ByteBuffer {

	private byte[] bytes;
	int index = 0;

	public ByteBuffer(byte[] bytes) {
		this.bytes = bytes;
	}

	public ByteBuffer(int length) {
		this.bytes = new byte[length];
	}

	public boolean hasBytes() {
		return index < bytes.length;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void write(int value, int length) {
		byte[] valueBytes = BigInteger.valueOf(value).toByteArray();

		if (valueBytes.length > length) {
			throw new IllegalArgumentException("value length is greater than allowed length");
		}

		for (int i = 0; i < length - valueBytes.length; i++) {
			bytes[index + i] = 0;
		}

		for (int i = 0; i < valueBytes.length; i++) {
			bytes[index + i + length - valueBytes.length] = valueBytes[i];
		}

		index += length;
	}

	public int readInt(int length) {
		int value = new BigInteger(Arrays.copyOfRange(bytes, index, index + length)).intValue();
		index += length;
		return value;
	}
}
