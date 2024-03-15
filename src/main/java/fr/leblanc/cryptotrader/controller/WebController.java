package fr.leblanc.cryptotrader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.leblanc.cryptotrader.service.CryptoPriceService;

@Controller
public class WebController {

	@Autowired
	private CryptoPriceService cryptoPriceService;
	
	@GetMapping("/chart")
	public String chart(Model model) {
		model.addAttribute("cryptoPrices", cryptoPriceService.findAll());
		return "chart";
	}
}
