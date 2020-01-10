package com.bromleyoil.mhw;

import java.util.Map.Entry;

import org.springframework.util.Base64Utils;

import com.bromleyoil.mhw.model.Decoration;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.EquipmentType;
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

		// Add the ID and count for each decoration
		for (Entry<Decoration, Integer> entry : equipmentSet.getDecorationCounts()) {
			buffer.write(entry.getKey().getId(), 2);
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
			} else {
				equipmentSet.add(equipmentList.getAny(EquipmentType.values()[i]));
			}
		}

		// Load the number of weapon slots
		equipmentSet.setWeaponSlotSet(SlotSet.of(buffer.readInt(1), buffer.readInt(1), buffer.readInt(1),
				buffer.readInt(1)));

		// Load the decorations
		while (buffer.hasBytes()) {
			int id = buffer.readInt(2);
			int level = buffer.readInt(1);
			Decoration decoration = equipmentList.getDecorations().get(id - 1);
			equipmentSet.decorate(decoration, level);
		}

		return equipmentSet;
	}
}
