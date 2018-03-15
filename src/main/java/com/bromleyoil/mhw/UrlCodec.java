package com.bromleyoil.mhw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.util.Base64Utils;

import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Skill;

/**
 * Encodes and decodes objects to and from a URL-safe Base64 representation.
 */
public class UrlCodec {

	private UrlCodec() {
		// Static utility class
	}

	public static String encode(EquipmentSet equipmentSet) {
		List<Integer> ints = new ArrayList<>();

		// Add the ID for each piece of equipment, or 0 if missing
		for (EquipmentType type : EquipmentType.values()) {
			ints.add(equipmentSet.get(type) != null ? equipmentSet.get(type).getId() : 0);
		}

		// Add the ID and count for each decorated skill
		for (Entry<Skill, Integer> entry : equipmentSet.getDecorationCounts()) {
			ints.add(entry.getKey().ordinal());
			ints.add(entry.getValue());
		}

		// Convert the IDs to a list of bytes, padding all values to 2 bytes
		byte[] bytes = new byte[EquipmentType.values().length * 2 + equipmentSet.getDecorationCounts().size() * 4];
		for (int i = 0; i < ints.size(); i++) {
			writeToBytes(bytes, i * 2, ints.get(i), 2);
		}

		return Base64Utils.encodeToUrlSafeString(bytes);
	}

	public static EquipmentSet decode(EquipmentList equipmentList, String encodedString) {
		byte[] bytes = Base64Utils.decodeFromUrlSafeString(encodedString);
		EquipmentSet equipmentSet = new EquipmentSet();

		for (int i = 0; i < EquipmentType.values().length; i++) {
			// Load an equipment ID
			int id = convertToInt(bytes, i * 2, 2);
			if (id > 0) {
				equipmentSet.add(equipmentList.getItems().get(id - 1));
			}
		}

		for (int i = EquipmentType.values().length * 2; i < bytes.length; i += 4) {
			// Load a decoration skill and count
			Skill skill = Skill.values()[convertToInt(bytes, i, 2)];
			int level = convertToInt(bytes, i + 2, 2);
			equipmentSet.decorate(skill, level);
		}

		return equipmentSet;
	}

	protected static void writeToBytes(byte[] bytes, int index, int value, int length) {
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
	}

	protected static int convertToInt(byte[] bytes, int index, int length) {
		return new BigInteger(Arrays.copyOfRange(bytes, index, index + length)).intValue();
	}
}
