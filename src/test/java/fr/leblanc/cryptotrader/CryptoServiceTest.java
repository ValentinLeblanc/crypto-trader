package fr.leblanc.cryptotrader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import fr.leblanc.cryptotrader.model.CryptoContext;
import fr.leblanc.cryptotrader.model.CryptoPrice;
import fr.leblanc.cryptotrader.service.CryptoService;
import fr.leblanc.cryptotrader.utils.ResourceUtils;

@SpringBootTest
@ActiveProfiles("test")
class CryptoServiceTest {

	@Autowired
	private CryptoService cryptoService;
	
	@Test
	void performanceTest() {
		JSONObject cryptoPriceData = ResourceUtils.parseJSON("./src/main/resources/cryptoPriceData.json");
		JSONArray cryptoPriceArray = cryptoPriceData.getJSONArray("cryptoPriceArray");
		List<CryptoPrice> cryptoPrices = new ArrayList<>();
		for (int i = 0; i < cryptoPriceArray.length(); i++) {
			JSONObject cryptoPriceJson = cryptoPriceArray.getJSONObject(i);
			cryptoPrices.add(new CryptoPrice(cryptoPriceJson.getLong("date"), cryptoPriceJson.getDouble("price")));
		}
		
		CryptoContext cryptoContext = new CryptoContext();
		
		int drops = 0;
		
		for (CryptoPrice cryptoPrice : cryptoPrices) {
			if (!cryptoService.check(cryptoContext, cryptoPrice)) {
				drops++;
			}
		}
		
		System.out.println("start=" + new Date(cryptoPrices.get(0).date()));
		System.out.println("size=" + cryptoPrices.size());
		System.out.println("drops=" + drops);
		System.out.println("final value=" + cryptoContext.getValue());
	}
	
}
