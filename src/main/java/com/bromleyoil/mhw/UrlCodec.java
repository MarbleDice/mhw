package com.bromleyoil.mhw;

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
		// NOTE: the buffer size must be adjusted for the length of serialized data
		ByteBuffer buffer = new ByteBuffer(EquipmentType.values().length * 2
				+ SlotSet.MAX_SLOT_LEVEL
				+ equipmentSet.getDecorationCounts().size() * 3);

		// Add the ID for each piece of equipment, or 0 if missing
		for (EquipmentType type : EquipmentType.values()) {
			int id = equipmentSet.get(type) != null ? equipmentSet.get(type).getId() : 0;
			buffer.write(id, 2);
		}

		// Add the number of weapon slots
		for (int level = 1; level <= SlotSet.MAX_SLOT_LEVEL; level++) {
			buffer.write(equipmentSet.getWeaponSlotSet().getSlotCount(level), 1);
		}

		// Add the ID and count for each decorated skill
		for (Entry<Skill, Integer> entry : equipmentSet.getDecorationCounts()) {
			buffer.write(entry.getKey().ordinal(), 2);
			buffer.write(entry.getValue(), 1);
		}

		return Base64Utils.encodeToUrlSafeString(buffer.getBytes());
	}

	public static EquipmentSet decode(EquipmentList equipmentList, String encodedString) {
		ByteBuffer buffer = new ByteBuffer(Base64Utils.decodeFromUrlSafeString(encodedString));
		EquipmentSet equipmentSet = new EquipmentSet();

		// Load equipment IDs, two bytes each
		for (int i = 0; i < EquipmentType.values().length; i++) {
			int id = buffer.readInt(2);
			if (id > 0) {
				equipmentSet.add(equipmentList.getItems().get(id - 1));
			}
		}

		// Load the number of weapon slots
		equipmentSet.setWeaponSlotSet(SlotSet.of(buffer.readInt(1), buffer.readInt(1), buffer.readInt(1),
				buffer.readInt(1)));

		// Load the decorated skills
		while (buffer.hasBytes()) {
			Skill skill = Skill.values()[buffer.readInt(2)];
			int level = buffer.readInt(1);
			equipmentSet.decorate(skill, level);
		}

		return equipmentSet;
	}

	public String encode(SetBuilderForm form) {
		ByteBuffer buffer = new ByteBuffer(3 + form.getSkillRows().size() * 4);

		buffer.write(form.getWeaponSlots4(), 1);
		buffer.write(form.getWeaponSlots3(), 1);
		buffer.write(form.getWeaponSlots2(), 1);
		buffer.write(form.getWeaponSlots1(), 1);
		buffer.write(form.getRequiredSlots4(), 1);
		buffer.write(form.getRequiredSlots3(), 1);
		buffer.write(form.getRequiredSlots2(), 1);
		buffer.write(form.getRequiredSlots1(), 1);

		for (SkillRow row : form.getSkillRows()) {
			buffer.write(row.getSkill().ordinal(), 2);
			buffer.write(row.getLevel() != null ? row.getLevel() : 0, 1);
			buffer.write(row.getDecorationCount() != null ? row.getDecorationCount() : 0, 1);
		}

		return Base64Utils.encodeToUrlSafeString(buffer.getBytes());
	}

	public SetBuilderForm decodeSetBuilderForm(String encodedString) {
		ByteBuffer buffer = new ByteBuffer(Base64Utils.decodeFromUrlSafeString(encodedString));
		SetBuilderForm form = new SetBuilderForm();

		form.setWeaponSlots4(buffer.readInt(1));
		form.setWeaponSlots3(buffer.readInt(1));
		form.setWeaponSlots2(buffer.readInt(1));
		form.setWeaponSlots1(buffer.readInt(1));
		form.setRequiredSlots4(buffer.readInt(1));
		form.setRequiredSlots3(buffer.readInt(1));
		form.setRequiredSlots2(buffer.readInt(1));
		form.setRequiredSlots1(buffer.readInt(1));

		while (buffer.hasBytes()) {
			form.addSkillRow(new SkillRow(Skill.values()[buffer.readInt(2)], buffer.readInt(1), buffer.readInt(1)));
		}

		return form;
	}
}
