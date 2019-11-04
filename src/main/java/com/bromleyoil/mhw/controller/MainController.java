package com.bromleyoil.mhw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.mhw.UrlCodec;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.EquipmentSet;
import com.bromleyoil.mhw.model.EquipmentType;
import com.bromleyoil.mhw.model.Skill;

@Controller
public class MainController {

	@Autowired
	private EquipmentList equipmentList;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/skill-list")
	public ModelAndView skillList() {
		ModelAndView mav = new ModelAndView("skill-list");

		mav.addObject("skills", Skill.values());

		return mav;
	}

	@GetMapping("/skill/{skillName}")
	public ModelAndView skill(@PathVariable String skillName) {
		ModelAndView mav = new ModelAndView("skill");

		Skill skill = Skill.valueOfName(skillName);
		mav.addObject("skill", skill);
		mav.addObject("charms", equipmentList.filter(skill).filter(EquipmentType.CHARM).getItems());
		mav.addObject("decorations", equipmentList.filter(skill).getDecorations());
		mav.addObject("matrix", equipmentList.filter(skill).getMatrix());

		return mav;
	}

	@GetMapping("/equipment-list")
	public ModelAndView equipmentList() {
		ModelAndView mav = new ModelAndView("equipment-list");

		mav.addObject("matrix", equipmentList.getMatrix());

		return mav;
	}

	@GetMapping("/equipment-set/{encodedSet}")
	public ModelAndView equipmentSet(@PathVariable String encodedSet) {
		ModelAndView mav = new ModelAndView("equipment-set");

		EquipmentSet set = UrlCodec.decode(equipmentList, encodedSet);
		mav.addObject("set", set);

		return mav;
	}
}
