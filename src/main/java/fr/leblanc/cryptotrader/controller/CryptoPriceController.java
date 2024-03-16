package fr.leblanc.cryptotrader.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.leblanc.cryptotrader.service.CryptoPriceService;

@RestController("/crypto-price")
public class CryptoPriceController {
	
	private CryptoPriceService cryptoPriceService;
	
	public CryptoPriceController(CryptoPriceService cryptoPriceService) {
		super();
		this.cryptoPriceService = cryptoPriceService;
	}

	@GetMapping("/import")
	public void importCryptoPrices(@RequestParam(required = false) String days) {
		cryptoPriceService.launchImportCryptoPriceJob(days);
	}
	
	@GetMapping("/reset")
	public void resetCryptoPrices() {
		cryptoPriceService.resetCryptoPrices();
	}
	
}
