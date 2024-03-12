package fr.leblanc.cryptotrader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.leblanc.cryptotrader.model.CryptoContext;
import fr.leblanc.cryptotrader.model.CryptoPrice;

@Service
public class CryptoService {

	@Value("${crypto.hysteresis.ratio}")
	private Double hysteresisRatio;

	public boolean check(CryptoContext cryptoContext, CryptoPrice newCryptoPrice) {
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
			double diffValue = newCryptoPrice.price() - (cryptoContext.getCryptoPriceList().isEmpty() ? 0d
					: cryptoContext.getCryptoPriceList().get(cryptoContext.getCryptoPriceList().size() - 1).price());
			cryptoContext.setValue(cryptoContext.getValue() + diffValue);
		}

		cryptoContext.getCryptoPriceList().add(newCryptoPrice);
		return cryptoContext.isActive();
	}

}
