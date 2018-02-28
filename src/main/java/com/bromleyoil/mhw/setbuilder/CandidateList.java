package com.bromleyoil.mhw.setbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;

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
			Superiority superiority = Superiority.compare(potential, existing, requiredSkillSet.getSkills());
			if (superiority == Superiority.WORSE) {
				// Potential candidate is worse than an existing candidate, so it should not be used
				log.debug("Will not add " + potential.getFullDescription());
				return;
			} else if (superiority == Superiority.BETTER) {
				// Potential candidate is better than an existing candidate which should be removed
				iterator.remove();
				log.debug("  Removing " + existing.getFullDescription());
			}
		}

		// The potential candidate is not worse than any existing one, so it should be added
		candidates.get(potential.getType()).add(potential);
		log.debug("Adding " + potential.getFullDescription());
	}

	public List<Equipment> getCandidates(EquipmentType type) {
		return candidates.get(type);
	}

	public int size(EquipmentType type) {
		return candidates.get(type).size();
	}

	public int size() {
		return candidates.entrySet().stream().map(x -> x.getValue().size())
				.collect(Collectors.reducing(1, (a, b) -> a * b));
	}

	@Override
	public String toString() {
		List<String> lines = new ArrayList<>();
		lines.add("Candidates");
		for (EquipmentType type : EquipmentType.values()) {
			lines.add("  " + type + ": " + getCandidates(type).size());
			for (Equipment equipment : getCandidates(type)) {
				lines.add("    " + equipment.getFullDescription());
			}
		}
		return String.join("\n", lines);
	}
}
