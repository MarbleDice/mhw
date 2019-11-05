package com.bromleyoil.mhw.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.comparator.Comparators;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bromleyoil.mhw.form.SetBuilderForm;
import com.bromleyoil.mhw.form.SkillRow;
import com.bromleyoil.mhw.model.Rank;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.setbuilder.ParallelSetBuilder;

@Controller
@RequestMapping("/set-builder")
public class SetBuilderController {

	private static final String VIEW = "set-builder";

	@Autowired
	private ParallelSetBuilder setBuilder;

	@ModelAttribute
	public List<Skill> getSkillList() {
		return Arrays.asList(Skill.values());
	}

	@ModelAttribute
	public List<Rank> getRankList() {
		return Arrays.stream(Rank.values()).sorted(Comparators.comparable().reversed()).collect(Collectors.toList());
	}

	@GetMapping
	public String initialRequest(SetBuilderForm form) {
		return VIEW;
	}

	@PostMapping(params = "addSkill")
	public String addSkill(SetBuilderForm form, BindingResult bindingResult, ModelMap modelMap) {
		modelMap.put("autofocus", form.getNewSkill());
		form.addSkillRow(new SkillRow(form.getNewSkill()));
		form.setNewSkill(null);
		return VIEW;
	}

	@PostMapping(params = "removeSkill")
	public String removeSkill(SetBuilderForm form, BindingResult bindingResult, HttpServletRequest request) {
		int index = Integer.parseInt(request.getParameter("removeSkill"));
		form.getSkillRows().remove(index);
		return VIEW;
	}

	@PostMapping(params = "search")
	public String search(SetBuilderForm form, BindingResult bindingResult, ModelMap modelMap) {
		if (bindingResult.hasErrors()) {
			return VIEW;
		}

		modelMap.put("result", setBuilder.search(form));

		return VIEW;
	}
}
