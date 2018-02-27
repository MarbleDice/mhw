package com.bromleyoil.mhw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.Skill;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@EnableAutoConfiguration
@ComponentScan
@Controller
public class MainController {

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

	@Autowired
	private EquipmentList equipmentList;

	public static void main(String[] args) {
		SpringApplication.run(MainController.class, args);
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/skill-list")
	public ModelAndView skillList() {
		ModelAndView mav = new ModelAndView("skill-list");

		mav.addObject("skills", Skill.values());

		return mav;
	}

	@RequestMapping("/skill/{skillName}")
	public ModelAndView skill(@PathVariable String skillName) {
		ModelAndView mav = new ModelAndView("skill");

		Skill skill = Skill.valueOfName(skillName);
		mav.addObject("skill", skill);
		mav.addObject("matrix", equipmentList.filter(skill).getMatrix());

		return mav;
	}

	@RequestMapping("/equipment-list")
	public ModelAndView equipmentList() {
		ModelAndView mav = new ModelAndView("equipment-list");

		mav.addObject("matrix", equipmentList.getMatrix());

		return mav;
	}

	@RequestMapping("/set-builder")
	public ModelAndView setBuilder() {
		ModelAndView mav = new ModelAndView("set-builder");
		mav.addObject("skills", Skill.values());
		return mav;
	}
}
