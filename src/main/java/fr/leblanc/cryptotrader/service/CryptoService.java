package fr.leblanc.cryptotrader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.leblanc.cryptotrader.model.CryptoContext;
import fr.leblanc.cryptotrader.model.CryptoPrice;

@Service
public class CryptoService {

	private final Double hysteresisRatio;

	public CryptoService(@Value("${crypto.hysteresis.ratio}") Double hysteresisRatio) {
		super();
		this.hysteresisRatio = hysteresisRatio;
	}

	public void optimizeGain(CryptoContext cryptoContext, CryptoPrice newCryptoPrice) {
		if (cryptoContext.isActive()) {
			if (newCryptoPrice.price() > cryptoContext.getOptimum()) {
				cryptoContext.setOptimum(newCryptoPrice.price());
			} else if (newCryptoPrice.price() < hysteresisRatio * cryptoContext.getOptimum()) {
				cryptoContext.setActive(false);
			}
		} else {
			if (newCryptoPrice.price() < cryptoContext.getOptimum()) {
				cryptoContext.setOptimum(newCryptoPrice.price());
			} else if (newCryptoPrice.price() > hysteresisRatio * cryptoContext.getOptimum()) {
				cryptoContext.setActive(true);
			}
		}
		if (cryptoContext.isActive()) {
			if (cryptoContext.getValue() == null) {
				cryptoContext.setValue(100d);
			} else {
				CryptoPrice previousCryptoPrice = cryptoContext.getCryptoPriceList().get(cryptoContext.getCryptoPriceList().size() - 1);
				double diffValue = (newCryptoPrice.price() - previousCryptoPrice.price()) * 100 / previousCryptoPrice.price();
				cryptoContext.setValue(cryptoContext.getValue() + diffValue);
			}
		}
		cryptoContext.getCryptoPriceList().add(newCryptoPrice);
	}

}
