package com.bromleyoil.mhw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.Formatter;

import com.bromleyoil.mhw.form.SkillFormatter;
import com.bromleyoil.mhw.model.Skill;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@EnableAutoConfiguration
@ComponentScan
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

	@Bean
	public Formatter<Skill> skillFormatter() {
		return new SkillFormatter();
	}
}
