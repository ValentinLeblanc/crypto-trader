package fr.leblanc.cryptotrader.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.leblanc.cryptotrader.model.CryptoPrice;
import fr.leblanc.cryptotrader.repository.CryptoPriceRepository;

@Service
public class CryptoPriceService {

	@Autowired
	private CryptoPriceRepository cryptoPriceRepository;
	
	public List<CryptoPrice> findAll() {
		return cryptoPriceRepository.findAll();
	}
	
}
