package com.bromleyoil.mhw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.Skill;

@EnableAutoConfiguration
@ComponentScan
@Controller
public class MainController {

	@Autowired
	private EquipmentList equipmentList;

	@RequestMapping("/")
	public String home() {
		return "home";
	}

	@RequestMapping("/skill-list")
	public ModelAndView skillList() {
		ModelAndView mav = new ModelAndView("skill-list");

		mav.addObject("skills", Skill.values());

		return mav;
	}

	@RequestMapping("/equipment-list")
	public ModelAndView equipmentList() {
		ModelAndView mav = new ModelAndView("equipment-list");

		mav.addObject("matrix", equipmentList.getMatrix());

		return mav;
	}

	public static void main(String[] args) {
		SpringApplication.run(MainController.class, args);
	}
}
