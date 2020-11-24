package io.javabrains.coronavirustracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.javabrains.coronavirustracker.services.CoronaVirusDataService;

@Controller
public class HomeContoller {

	@Autowired
	CoronaVirusDataService  coronaVirusDataService;
	
	@GetMapping("/")
	public String home(Model model)
	{
		model.addAttribute( "locationStats" , coronaVirusDataService.getAllStats());
		return "home";
	}
}
