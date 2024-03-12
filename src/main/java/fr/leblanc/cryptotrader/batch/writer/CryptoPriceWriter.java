package fr.leblanc.cryptotrader.batch.writer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import fr.leblanc.cryptotrader.model.CryptoPrice;
import fr.leblanc.cryptotrader.utils.ResourceUtils;

public class CryptoPriceWriter implements ItemWriter<CryptoPrice>, StepExecutionListener {

	private static final String OUTPUT_FILE = "./src/main/resources/cryptoPriceData.json";

	private static final Logger logger = LoggerFactory.getLogger(CryptoPriceWriter.class);

	private JSONArray cryptoPricesJsonArray = new JSONArray();

	@Override
	public void write(Chunk<? extends CryptoPrice> chunk) throws Exception {
		for (CryptoPrice cryptoPrice : chunk.getItems()) {
			JSONObject cryptoPriceJson = new JSONObject();
			cryptoPriceJson.put("date", cryptoPrice.date());
			cryptoPriceJson.put("price", cryptoPrice.price());
			cryptoPricesJsonArray.put(cryptoPriceJson);
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		JSONObject cryptoPriceData = new JSONObject();
		cryptoPriceData.put("date", System.currentTimeMillis());
		cryptoPriceData.put("cryptoPriceArray", cryptoPricesJsonArray);
		ResourceUtils.storeJSON(cryptoPriceData, OUTPUT_FILE);
		logger.info("'cryptoPriceData.json' successfully created");
		return ExitStatus.COMPLETED;
	}

}
