package com.bromleyoil.mhw.setbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;

public class CandidateList {

	private static Logger log = LoggerFactory.getLogger(CandidateList.class);

	private SkillSet requiredSkillSet;
	private EnumMap<EquipmentType, List<Equipment>> candidates;

	public CandidateList(EquipmentList equipmentList, SkillSet requiredSkillSet) {
		this.requiredSkillSet = requiredSkillSet;
		buildCandidates(equipmentList);
	}

	protected void buildCandidates(EquipmentList equipmentList) {
		// Initialize the candidates
		candidates = new EnumMap<>(EquipmentType.class);
		for (EquipmentType type : EquipmentType.values()) {
			candidates.put(type, new ArrayList<>());
		}

		// Add all gear with matching skills or slots
		for (Equipment equipment : equipmentList.getItems()) {
			Set<Skill> equipmentSkills = equipment.getSkillSet().getSkills();

			// Non-disjoint sets mean there is overlap
			if (!Collections.disjoint(requiredSkillSet.getSkills(), equipmentSkills) || equipment.hasSlots()) {
				addCandidate(equipment);
			}
		}
	}

	public final Comparator<Equipment> superiorOrder = (a, b) -> {
		int rv;
		boolean isBetter = false;
		boolean isWorse = false;

		// Compare the equipment skill values for all required skills
		for (Skill skill : requiredSkillSet.getSkills()) {
			rv = Integer.compare(a.getValue(skill), b.getValue(skill));
			isBetter |= rv > 0;
			isWorse |= rv < 0;
		}
		log.info("    Skills {} {}", (isBetter ? " better" : ""), (isWorse ? " worse" : ""));
		// Compare equipment slots
		rv = SlotSet.SUPERIORITY.compare(a.getSlotSet(), b.getSlotSet());
		isBetter |= rv > 0;
		isWorse |= rv < 0;
		log.info("    Skills+slots {} {}", (isBetter ? " better" : ""), (isWorse ? " worse" : ""));

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
				log.info("Will not add " + potential.getFullDescription());
				return;
			} else if (rv > 0) {
				// Potential candidate is better than an existing candidate which should be removed
				iterator.remove();
				log.info("  Removing " + existing.getFullDescription());
			}
		}

		// The potential candidate is not worse than any existing one, so it should be added
		candidates.get(potential.getType()).add(potential);
		log.info("Adding " + potential.getFullDescription());
	}

	public List<Equipment> getCandidates(EquipmentType type) {
		return candidates.get(type);
	}
}
