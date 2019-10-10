package com.bromleyoil.mhw.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.form.SkillRow;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;
import com.bromleyoil.mhw.setbuilder.SetBuilder;

@Controller
@RequestMapping("/set-builder")
public class SetBuilderController {

	private static final String VIEW = "set-builder";

	@Autowired
	private SetBuilder setBuilder;

	@ModelAttribute
	public List<Skill> getSkillList() {
		return Arrays.asList(Skill.values());
	}

	@RequestMapping
	public String initialRequest(SetBuilderForm form) {
		return VIEW;
	}

	@RequestMapping(params = "addSkill")
	public String addSkill(SetBuilderForm form, BindingResult bindingResult, ModelMap modelMap) {
		modelMap.put("autofocus", form.getNewSkill());
		form.addSkillRow(new SkillRow(form.getNewSkill()));
		form.setNewSkill(null);
		return VIEW;
	}

	@RequestMapping(params = "removeSkill")
	public String removeSkill(SetBuilderForm form, BindingResult bindingResult, HttpServletRequest request) {
		int index = Integer.parseInt(request.getParameter("removeSkill"));
		form.getSkillRows().remove(index);
		return VIEW;
	}

	@RequestMapping(params = "search")
	public String search(SetBuilderForm form, BindingResult bindingResult, ModelMap modelMap) {
		if (bindingResult.hasErrors()) {
			return VIEW;
		}
		setBuilder.setRequiredSkillSet(new SkillSet(form.getSkills(), form.getLevels()));

		setBuilder.setRequiredSlotSet(new SlotSet(form.getRequiredSlots4(), form.getRequiredSlots3(),
				form.getRequiredSlots2(), form.getRequiredSlots1()));

		setBuilder.setWeaponSlotSet(new SlotSet(form.getWeaponSlots4(), form.getWeaponSlots3(),
				form.getWeaponSlots2(), form.getWeaponSlots1()));

		setBuilder.setDecorationCounts(form.getDecorationCounts());

		modelMap.put("result", setBuilder.search());

		return VIEW;
	}
}
