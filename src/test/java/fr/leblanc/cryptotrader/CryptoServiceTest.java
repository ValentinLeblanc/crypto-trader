package fr.leblanc.cryptotrader;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.leblanc.cryptotrader.model.CryptoContext;
import fr.leblanc.cryptotrader.model.CryptoPrice;
import fr.leblanc.cryptotrader.service.CryptoService;
import fr.leblanc.cryptotrader.utils.ResourceUtils;

class CryptoServiceTest {

	@ParameterizedTest
	@ValueSource(doubles = { 0.9, 0.99, 0.999, 0.9999 })
	void optimizeGain1DayTest(double hysteresisRatio) {
		CryptoService cryptoService = new CryptoService(hysteresisRatio);
    	optimizeGain(hysteresisRatio, cryptoService, "12-03-24,1d");
	}
	
	@ParameterizedTest
	@ValueSource(doubles = { 0.9, 0.99, 0.999, 0.9999 })
	void optimizeGain10DayTest(double hysteresisRatio) {
		CryptoService cryptoService = new CryptoService(hysteresisRatio);
		optimizeGain(hysteresisRatio, cryptoService, "12-03-24,10d");
	}

	private void optimizeGain(double hysteresisRatio, CryptoService cryptoService, String filePath) {
		JSONObject cryptoPriceData = ResourceUtils.parseJSON("./src/test/resources/" + filePath + ".json");
		JSONArray cryptoPriceArray = cryptoPriceData.getJSONArray("cryptoPriceArray");
		List<CryptoPrice> cryptoPrices = new ArrayList<>();
		for (int i = 0; i < cryptoPriceArray.length(); i++) {
			JSONObject cryptoPriceJson = cryptoPriceArray.getJSONObject(i);
			cryptoPrices.add(new CryptoPrice(cryptoPriceJson.getLong("date"), cryptoPriceJson.getDouble("price")));
		}
		CryptoContext cryptoContext = new CryptoContext();
		int drops = 0;
		for (CryptoPrice cryptoPrice : cryptoPrices) {
			cryptoService.optimizeGain(cryptoContext, cryptoPrice);
			if (!cryptoContext.isActive()) {
				drops++;
			}
		}
		System.out.println(filePath);
		System.out.println("ratio = " + hysteresisRatio);
		System.out.println("activity = " + (cryptoPrices.size() - drops) * 100 / cryptoPrices.size() + " %");
		System.out.println("gain = " + cryptoContext.getValue());
		System.out.println("");
		assertThat(cryptoContext.getValue()).isNotNull();
	}

}
