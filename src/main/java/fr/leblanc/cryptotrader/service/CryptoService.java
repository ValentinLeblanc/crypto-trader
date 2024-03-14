package fr.leblanc.cryptotrader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.leblanc.cryptotrader.model.CryptoTracker;

@Service
public class CryptoService {

	private final Double hysteresisRatio;

	public CryptoService(@Value("${crypto.hysteresis.ratio}") Double hysteresisRatio) {
		super();
		this.hysteresisRatio = hysteresisRatio;
	}

	public void updateTracker(CryptoTracker cryptoTracker, double newPrice) {
		cryptoTracker.addIteration();
		if (cryptoTracker.isActive()) {
			double gain = newPrice - cryptoTracker.getCurrentPrice();
			cryptoTracker.addGain(gain);
			if (newPrice > cryptoTracker.getOptimum()) {
				cryptoTracker.setOptimum(newPrice);
			} else if (newPrice < hysteresisRatio * cryptoTracker.getOptimum()) {
				cryptoTracker.setOptimum(newPrice);
				cryptoTracker.setActive(false);
			}
		} else {
			if (newPrice < cryptoTracker.getOptimum()) {
				cryptoTracker.setOptimum(newPrice);
			} else if (newPrice > (1 + (1 - hysteresisRatio) / 10) * cryptoTracker.getOptimum()) {
				cryptoTracker.setOptimum(newPrice);
				cryptoTracker.setActive(true);
			}
		}
		cryptoTracker.setCurrentPrice(newPrice);
	}

}
