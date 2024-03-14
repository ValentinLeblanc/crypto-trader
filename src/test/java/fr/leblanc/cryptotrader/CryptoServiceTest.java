package fr.leblanc.cryptotrader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.leblanc.cryptotrader.model.CryptoPrice;
import fr.leblanc.cryptotrader.model.CryptoTracker;
import fr.leblanc.cryptotrader.service.CryptoService;
import fr.leblanc.cryptotrader.utils.ResourceUtils;

class CryptoServiceTest {

	@ParameterizedTest
	@ValueSource(doubles = { 0, 0.9, 0.99, 0.999, 1 })
	void gainTestSample(double hysteresisRatio) {
		CryptoService cryptoService = new CryptoService(hysteresisRatio);
    	gainTest(hysteresisRatio, cryptoService, "13-03-24,1d");
	}

	private void gainTest(double hysteresisRatio, CryptoService cryptoService, String filePath) {
		JSONObject cryptoPriceData = ResourceUtils.parseJSON("./src/test/resources/" + filePath + ".json");
		JSONArray cryptoPriceArray = cryptoPriceData.getJSONArray("cryptoPriceArray");
		List<CryptoPrice> cryptoPrices = new ArrayList<>();
		for (int i = 0; i < cryptoPriceArray.length(); i++) {
			JSONObject cryptoPriceJson = cryptoPriceArray.getJSONObject(i);
			cryptoPrices.add(new CryptoPrice(cryptoPriceJson.getLong("date"), cryptoPriceJson.getDouble("price")));
		}
		CryptoTracker cryptoTracker = new CryptoTracker(cryptoPrices.get(0).price());
		for (CryptoPrice cryptoPrice : cryptoPrices) {
			cryptoService.updateTracker(cryptoTracker, cryptoPrice.price());
		}
		System.out.println(filePath);
		System.out.println("ratio = " + hysteresisRatio);
		System.out.println("activity = " + cryptoTracker.getActivity() + " %");
		System.out.println("gain = " + cryptoTracker.getGain() + " %");
		System.out.println("");
		assertThat(cryptoTracker.getGain()).isNotNull();
	}

	@Test
	void updateTrackerTest() {
		CryptoService cryptoService = new CryptoService(0.5);
		CryptoTracker cryptoTracker = new CryptoTracker(100d);
		
		cryptoService.updateTracker(cryptoTracker, 150);
		assertTrue(cryptoTracker.isActive());
		assertEquals(50, cryptoTracker.getGain(), 0.001);
		
		cryptoService.updateTracker(cryptoTracker, 120);
		assertTrue(cryptoTracker.isActive());
		assertEquals(20, cryptoTracker.getGain(), 0.001);
		
		cryptoService.updateTracker(cryptoTracker, 50);
		assertFalse(cryptoTracker.isActive());
		assertEquals(-50, cryptoTracker.getGain(), 0.001);
		
		cryptoService.updateTracker(cryptoTracker, 60);
		assertFalse(cryptoTracker.isActive());
		assertEquals(-50, cryptoTracker.getGain(), 0.001);
		
		cryptoService.updateTracker(cryptoTracker, 110);
		assertTrue(cryptoTracker.isActive());
		assertEquals(-50, cryptoTracker.getGain(), 0.001);
		
	}
	
	@Test
	void gainTest() {
		CryptoService cryptoService = new CryptoService(0d);
		CryptoTracker cryptoTracker = new CryptoTracker(100d);
		
		double[] prices = { 110, 125, 117, 132, 133, 142, 135, 1500, 1550 };
		
		for (double price : prices) {
			cryptoService.updateTracker(cryptoTracker, price);
		}
		
		System.out.println("gain for 0 = " + cryptoTracker.getGain());
		
		cryptoService = new CryptoService(0.5);
		cryptoTracker = new CryptoTracker(100d);
		
		for (double price : prices) {
			cryptoService.updateTracker(cryptoTracker, price);
		}
		
		System.out.println("gain for 0.5 = " + cryptoTracker.getGain());
		
		cryptoService = new CryptoService(0.9);
		cryptoTracker = new CryptoTracker(100d);
		
		for (double price : prices) {
			cryptoService.updateTracker(cryptoTracker, price);
		}
		
		System.out.println("gain for 0.9 = " + cryptoTracker.getGain());
		
		cryptoService = new CryptoService(0.99);
		cryptoTracker = new CryptoTracker(100d);
		
		for (double price : prices) {
			cryptoService.updateTracker(cryptoTracker, price);
		}
		
		System.out.println("gain for 0.99 = " + cryptoTracker.getGain());
		
	}
	
}
