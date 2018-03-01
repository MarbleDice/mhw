package com.bromleyoil.mhw.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.model.Skill;

@Controller
public class SetBuilderController {

	Logger log = LoggerFactory.getLogger(SetBuilderController.class);

	@ModelAttribute
	public List<Skill> getSkillList() {
		return Arrays.asList(Skill.values());
	}

	@RequestMapping("/set-builder")
	public ModelAndView initialRequest(@ModelAttribute SetBuilderForm form) {
		log.info("Form is {}", form);
		ModelAndView mav = new ModelAndView("set-builder");
		form = new SetBuilderForm();
		// mav.addObject("skills", Skill.values());
		mav.addObject("form", new SetBuilderForm());
		return mav;
	}

	@RequestMapping(value = "/set-builder", params = "addSkill")
	public String addRow(@ModelAttribute SetBuilderForm form) {
		log.info("Form is {}", form);
		log.info("Form has {} skills", form.getInterestingSkills().size());

		Random rng = new Random();
		form.getInterestingSkills().add(Skill.values()[rng.nextInt(10)]);
		form.getRequiredSkillLevels().add(0);

		// ModelAndView mav = new ModelAndView("set-builder");
		// mav.addObject("skills", Skill.values());
		// mav.addObject("form", form);

		return "/set-builder";
	}
}
