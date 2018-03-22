package com.bromleyoil.mhw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.util.Base64Utils;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.form.SkillRow;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SlotSet;

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

		// Add the number of weapon slots
		ints.add(equipmentSet.getWeaponSlotSet().getThree());
		ints.add(equipmentSet.getWeaponSlotSet().getTwo());
		ints.add(equipmentSet.getWeaponSlotSet().getOne());

		// Add the ID and count for each decorated skill
		for (Entry<Skill, Integer> entry : equipmentSet.getDecorationCounts()) {
			ints.add(entry.getKey().ordinal());
			ints.add(entry.getValue());
		}

		// Convert the IDs to a list of bytes, padding all values to 2 bytes
		byte[] bytes = new byte[EquipmentType.values().length * 2 + 6 + equipmentSet.getDecorationCounts().size() * 4];
		for (int i = 0; i < ints.size(); i++) {
			writeToBytes(bytes, i * 2, ints.get(i), 2);
		}

		return Base64Utils.encodeToUrlSafeString(bytes);
	}

	public static EquipmentSet decode(EquipmentList equipmentList, String encodedString) {
		byte[] bytes = Base64Utils.decodeFromUrlSafeString(encodedString);
		EquipmentSet equipmentSet = new EquipmentSet();
		int index = 0;

		// Load equipment IDs, two bytes each
		for (int i = 0; i < EquipmentType.values().length; i++) {
			int id = readToInt(bytes, index, 2);
			index += 2;
			if (id > 0) {
				equipmentSet.add(equipmentList.getItems().get(id - 1));
			}
		}

		// Load the number of weapon slots
		equipmentSet.setWeaponSlotSet(new SlotSet(readToInt(bytes, index, 2), readToInt(bytes, index + 2, 2),
				readToInt(bytes, index + 4, 2)));
		index += 6;

		// Load the decorated skills
		for (; index < bytes.length; index += 4) {
			Skill skill = Skill.values()[readToInt(bytes, index, 2)];
			int level = readToInt(bytes, index + 2, 2);
			equipmentSet.decorate(skill, level);
		}

		return equipmentSet;
	}

	public String encode(SetBuilderForm form) {
		List<Integer> values = new ArrayList<>();

		values.add(form.getRequiredSlots1());
		values.add(form.getRequiredSlots2());
		values.add(form.getRequiredSlots3());

		for (SkillRow row : form.getSkillRows()) {
			values.add(row.getSkill().ordinal());
			values.add(row.getLevel() != null ? row.getLevel() : 0);
			values.add(row.getDecorationCount() != null ? row.getDecorationCount() : 0);
		}

		byte[] bytes = new byte[3 + form.getSkillRows().size() * 4];
		for (int i = 0; i < 3; i++) {
			writeToBytes(bytes, i, values.get(i), 1);
		}

		int byteIndex = 3;
		for (int i = 3; i < values.size(); i += 3) {
			writeToBytes(bytes, byteIndex, values.get(i), 2);
			writeToBytes(bytes, byteIndex, values.get(i + 1), 1);
			writeToBytes(bytes, byteIndex, values.get(i + 2), 1);
			byteIndex += 4;
		}

		return Base64Utils.encodeToUrlSafeString(bytes);
	}

	public SetBuilderForm decodeSetBuilderForm(String encodedString) {
		byte[] bytes = Base64Utils.decodeFromUrlSafeString(encodedString);
		SetBuilderForm form = new SetBuilderForm();

		form.setRequiredSlots1(readToInt(bytes, 0, 1));
		form.setRequiredSlots2(readToInt(bytes, 1, 1));
		form.setRequiredSlots3(readToInt(bytes, 2, 1));

		return form;
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

	protected static int readToInt(byte[] bytes, int index, int length) {
		return new BigInteger(Arrays.copyOfRange(bytes, index, index + length)).intValue();
	}
}
