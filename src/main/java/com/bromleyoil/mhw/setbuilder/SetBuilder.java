package com.bromleyoil.mhw.setbuilder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.EquipmentType;

@Configurable
public class SetBuilder {

	@Autowired
	private EquipmentList equipmentList;

	private Map<EquipmentType, List<Equipment>> candidates;

	private List<EquipmentSet> solutions;

	public SetBuilder() {
		candidates = new EnumMap<>(EquipmentType.class);
		solutions = new ArrayList<>();
	}

	public SetBuilder(SetBuilder setBuilder) {
		this.candidates = setBuilder.candidates;
		this.solutions = setBuilder.solutions;
	}

	public List<EquipmentSet> search() {
		return null;
	}
}
