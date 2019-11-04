package com.bromleyoil.mhw.setbuilder;

import org.springframework.beans.factory.annotation.Autowired;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.model.EquipmentList;

public class ParallelSetBuilder implements SetBuilder {

	@Autowired
	private EquipmentList equipmentList;

	public ParallelSetBuilder() {

	}

	public ParallelSetBuilder(EquipmentList equipmentList) {
		this.equipmentList = equipmentList;
	}

	@Override
	public SearchResult search(SetBuilderForm form) {
		// EquipmentList el = equipmentList.filter(form)
		return null;
	}
}
