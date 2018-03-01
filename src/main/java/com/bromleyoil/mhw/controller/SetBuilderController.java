package com.bromleyoil.mhw.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.setbuilder.SearchResult;
import com.bromleyoil.mhw.setbuilder.SetBuilder;

@Controller
@RequestMapping("/set-builder")
public class SetBuilderController {

	Logger log = LoggerFactory.getLogger(SetBuilderController.class);

	@Autowired
	private EquipmentList equipmentList;

	@ModelAttribute
	public List<Skill> getSkillList() {
		return Arrays.asList(Skill.values());
	}

	@RequestMapping
	public ModelAndView initialRequest() {
		return modelAndView(new SetBuilderForm());
	}

	@RequestMapping(params = "addSkill")
	public ModelAndView addSkill(SetBuilderForm form, HttpServletRequest request) {
		form.getSkills().add(form.getNewSkill());
		form.getLevels().add(0);

		return modelAndView(form);
	}

	@RequestMapping(params = "removeSkill")
	public ModelAndView removeSkill(SetBuilderForm form, HttpServletRequest request) {
		int index = Integer.valueOf(request.getParameter("removeSkill"));
		form.getSkills().remove(index);
		form.getLevels().remove(index);

		return modelAndView(form);
	}

	@RequestMapping(params = "search")
	public ModelAndView search(SetBuilderForm form) {
		SetBuilder setBuilder = new SetBuilder();

		SearchResult result = setBuilder.search(equipmentList,
				new SkillSet(form.getSkills(), form.getLevels()));

		ModelAndView mav = modelAndView(form);
		mav.addObject("result", result);
		return mav;
	}

	protected ModelAndView modelAndView(SetBuilderForm form) {
		return new ModelAndView("set-builder", "form", form);
	}
}
