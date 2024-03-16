package fr.leblanc.cryptotrader.batch.reader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import fr.leblanc.cryptotrader.model.CryptoPrice;

public class CryptoPriceReader implements ItemReader<CryptoPrice>, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(CryptoPriceReader.class);
	
	private List<CryptoPrice> cryptoPrices = new ArrayList<>();
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		String days = stepExecution.getJobParameters().getString("days");
        String apiUrl = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=" + days;
        RestTemplate restTemplate = new RestTemplateBuilder().build();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            if (response.getStatusCode().value() == 200) {
            	JSONObject jsonResponse = new JSONObject(response.getBody());
            	JSONArray prices = jsonResponse.getJSONArray("prices");
            	for (int i = 0; i < prices.length(); i++) {
            		JSONArray priceEntry = prices.getJSONArray(i);
            		cryptoPrices.add(new CryptoPrice(priceEntry.getLong(0), priceEntry.getDouble(1)));
            	}
            } else {
            	logger.error("Error while fetching crypto data, response is: {}", response);
            }
        } catch (Exception e) {
        	logger.error("Error while fetching crypto data", e);
        }
	}
	
	@Override
	public CryptoPrice read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (!cryptoPrices.isEmpty()) {
			return cryptoPrices.remove(0);
		}
		return null;
	}

}
