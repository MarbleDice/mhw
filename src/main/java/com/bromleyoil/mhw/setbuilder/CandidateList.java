package com.bromleyoil.mhw.setbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillValue;
import com.bromleyoil.mhw.model.SlotSet;

public class CandidateList {

	private List<SkillValue> requiredSkillValues;
	private EnumMap<EquipmentType, List<Equipment>> candidates;

	public CandidateList(EquipmentList equipmentList, List<SkillValue> requiredSkillValues) {
		this.requiredSkillValues = requiredSkillValues;
		buildCandidates(equipmentList);
	}

	protected void buildCandidates(EquipmentList equipmentList) {
		// Initialize the candidates
		candidates = new EnumMap<>(EquipmentType.class);
		for (EquipmentType type : EquipmentType.values()) {
			candidates.put(type, new ArrayList<>());
		}

		Set<Skill> requiredSkills = requiredSkillValues.stream().map(SkillValue::getSkill).collect(Collectors.toSet());
		// Add all gear with matching skills or slots
		for (Equipment equipment : equipmentList.getItems()) {
			Set<Skill> equipmentSkills = equipment.getSkillValues().stream()
					.map(SkillValue::getSkill).collect(Collectors.toSet());

			// Non-disjoint sets mean there is overlap
			if (!Collections.disjoint(requiredSkills, equipmentSkills) || equipment.hasSlots()) {
				addCandidate(equipment);
			}
		}
	}

	public final Comparator<Equipment> superiorOrder = (a, b) -> {
		int rv;
		boolean isBetter = false;
		boolean isWorse = false;

		// Compare the equipment skill values for all required skills
		for (SkillValue skillValue : requiredSkillValues) {
			rv = Integer.compare(a.getValue(skillValue.getSkill()), b.getValue(skillValue.getSkill()));
			isBetter |= rv > 0;
			isWorse |= rv < 0;
		}

		// Compare equipment slots
		rv = SlotSet.SUPERIOR_ORDER.compare(a.getSlots(), b.getSlots());
		isBetter |= rv > 0;
		isWorse |= rv < 0;

		if (isBetter && !isWorse) {
			return 1;
		} else if (isWorse && !isBetter) {
			return -1;
		}

		return 0;
	};

	/**
	 * Add a potential candidate to the list of candidates. Removes existing candidates which are obsoleted by the
	 * potential candidate, and does not add the potential if it's worse than any existing candidate.
	 * 
	 * @param potential
	 */
	protected void addCandidate(Equipment potential) {
		Iterator<Equipment> iterator = candidates.get(potential.getType()).iterator();

		// Check the potential candidate against all existing candidates
		while (iterator.hasNext()) {
			Equipment existing = iterator.next();
			int rv = superiorOrder.compare(potential, existing);
			if (rv < 0) {
				// Potential candidate is worse than an existing candidate, so it should not be used
				return;
			} else if (rv > 0) {
				// Potential candidate is better than an existing candidate which should be removed
				iterator.remove();
			}
		}

		// The potential candidate is not worse than any existing one, so it should be added
		candidates.get(potential.getType()).add(potential);
	}

	public List<Equipment> getCandidates(EquipmentType type) {
		return candidates.get(type);
	}
}
