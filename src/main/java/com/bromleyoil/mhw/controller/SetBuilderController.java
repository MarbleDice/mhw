package com.bromleyoil.mhw.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.model.SkillSet;
import com.bromleyoil.mhw.model.SlotSet;
import com.bromleyoil.mhw.setbuilder.SearchResult;
import com.bromleyoil.mhw.setbuilder.SetBuilder;

@Controller
@RequestMapping("/set-builder")
public class SetBuilderController {

	Logger log = LoggerFactory.getLogger(SetBuilderController.class);

	@Autowired
	private SetBuilder setBuilder;

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
		ModelAndView mav = modelAndView(form);
		mav.addObject("autofocus", form.getNewSkill());
		form.addSkillLevel(form.getNewSkill(), null);
		form.setNewSkill(null);
		return mav;
	}

	@RequestMapping(params = "removeSkill")
	public ModelAndView removeSkill(SetBuilderForm form, HttpServletRequest request) {
		int index = Integer.parseInt(request.getParameter("removeSkill"));
		form.getSkills().remove(index);
		form.getLevels().remove(index);
		return modelAndView(form);
	}

	@RequestMapping(params = "search")
	public ModelAndView search(SetBuilderForm form) {
		setBuilder.setRequiredSkillSet(new SkillSet(form.getSkills(),
				form.getLevels().stream().map(x -> x != null ? x : 0).collect(Collectors.toList())));

		SlotSet requiredSlotSet = new SlotSet();
		List<Integer> slotLevels = Arrays.asList(form.getRequiredSlots1(), form.getRequiredSlots2(),
				form.getRequiredSlots3());
		for (int level = 0; level < slotLevels.size(); level++) {
			for (int i = 0; i < Optional.ofNullable(slotLevels.get(level)).orElse(0); i++) {
				requiredSlotSet.add(level + 1);
			}
		}
		setBuilder.setRequiredSlotSet(requiredSlotSet);

		SearchResult result = setBuilder.search();

		ModelAndView mav = modelAndView(form);
		mav.addObject("result", result);
		return mav;
	}

	protected ModelAndView modelAndView(SetBuilderForm form) {
		return new ModelAndView("set-builder", "form", form);
	}
}
