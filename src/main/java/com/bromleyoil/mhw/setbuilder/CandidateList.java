package com.bromleyoil.mhw.setbuilder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.SkillSet;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CandidateList {

	private static Logger log = LoggerFactory.getLogger(CandidateList.class);

	@Autowired
	private EquipmentList equipmentList;

	private SkillSet requiredSkillSet;
	private EnumMap<EquipmentType, List<Equipment>> candidates;
	private int filteredCandidateCount;

	public CandidateList() {
	}

	public CandidateList(EquipmentList equipmentList) {
		this.equipmentList = equipmentList;
	}

	protected void buildCandidates(SetBuilderForm form) {
		this.requiredSkillSet = form.getRequiredSkillSet();

		// Initialize the candidates
		candidates = new EnumMap<>(EquipmentType.class);
		for (EquipmentType type : EquipmentType.values()) {
			candidates.put(type, new ArrayList<>());
		}

		// Add all gear with matching skills or slots
		filteredCandidateCount = 0;
		for (Equipment equipment : equipmentList.getItems()) {
			boolean isRankFiltered = form.getMaxRank() != null && equipment.getRank() != null
					&& equipment.getRank().ordinal() > form.getMaxRank().ordinal();

			if (!isRankFiltered && (equipment.hasAnySkill(requiredSkillSet) || equipment.hasSlots())) {
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
				log.debug("Will not add {}", potential.getFullDescription());
				filteredCandidateCount++;
				return;
			} else if (superiority == Superiority.BETTER) {
				// Potential candidate is better than an existing candidate which should be removed
				log.debug("  Removing {}", existing.getFullDescription());
				filteredCandidateCount++;
				iterator.remove();
			}
		}

		// The potential candidate is not worse than any existing one, so it should be added
		log.debug("Adding {}", potential.getFullDescription());
		candidates.get(potential.getType()).add(potential);
	}

	public List<Equipment> getCandidates(EquipmentType type) {
		return candidates.get(type);
	}

	public int size(EquipmentType type) {
		return candidates.get(type).size();
	}

	public int size() {
		return candidates.entrySet().stream().map(x -> x.getValue().size())
				.collect(Collectors.reducing(0, (a, b) -> a + b));
	}

	public int getPermutationCount() {
		return candidates.entrySet().stream().map(x -> x.getValue().size())
				.collect(Collectors.reducing(1, (a, b) -> a * b));
	}

	public int getFilteredCandidateCount() {
		return filteredCandidateCount;
	}

	public void setFilteredCandidateCount(int filteredCandidateCount) {
		this.filteredCandidateCount = filteredCandidateCount;
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
