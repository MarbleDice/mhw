package com.bromleyoil.mhw.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.bromleyoil.mhw.model.Decoration;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.Rank;
import com.bromleyoil.mhw.model.Skill;
import com.bromleyoil.mhw.setbuilder.NaiveSetBuilder;
import com.bromleyoil.mhw.setbuilder.ParallelSetBuilder;
import com.bromleyoil.mhw.setbuilder.SetBuilder;

@Controller
@RequestMapping("/set-builder")
public class SetBuilderController {

	Logger log = LoggerFactory.getLogger(SetBuilderController.class);

	private static final String VIEW = "set-builder";

	@Autowired
	private EquipmentList equipmentList;

	@Autowired
	private NaiveSetBuilder naiveSetBuilder;

	@Autowired
	private ParallelSetBuilder parallelSetBuilder;

	@ModelAttribute(name = "skills")
	public List<Skill> skills() {
		return Arrays.stream(Skill.values()).filter(s -> !s.isWildcard()).collect(Collectors.toList());
	}

	@ModelAttribute(name = "ranks")
	public List<Rank> ranks() {
		return Arrays.stream(Rank.values()).sorted(Comparators.comparable().reversed()).collect(Collectors.toList());
	}

	@ModelAttribute(name = "baseDecorations")
	public Map<Skill, Decoration> baseDecorations() {
		return equipmentList.getBaseDecorations();
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
		StopWatch stopWatch = new StopWatch();

		if (bindingResult.hasErrors()) {
			return VIEW;
		}

		SetBuilder setBuilder;
		if ("Naive".equals(form.getAlgorithm())) {
			setBuilder = naiveSetBuilder;
		} else {
			setBuilder = parallelSetBuilder;
		}

		stopWatch.start();
		modelMap.put("result", setBuilder.search(form));
		stopWatch.stop();
		modelMap.put("time", String.format("%.3f", stopWatch.getTime() / 1000d));

		return VIEW;
	}
}
