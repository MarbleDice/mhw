package com.bromleyoil.mhw;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bromleyoil.mhw.model.Equipment;
import com.bromleyoil.mhw.model.EquipmentList;
import com.bromleyoil.mhw.model.Skill;

@EnableAutoConfiguration
@ComponentScan
@Controller
public class MainController {

	@Autowired
	private EquipmentList equipmentList;

	@RequestMapping("/")
	@ResponseBody
	String home() {
		StringBuilder sb = new StringBuilder();

		for (Entry<String, Equipment[]> entry : equipmentList.filter(Skill.EARPLUGS).getMatrix().entrySet()) {
			sb.append(entry.getKey());

			for (Equipment equipment : entry.getValue()) {
				sb.append(" | " + equipment);
			}

			sb.append(System.lineSeparator());
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		SpringApplication.run(MainController.class, args);
	}
}
