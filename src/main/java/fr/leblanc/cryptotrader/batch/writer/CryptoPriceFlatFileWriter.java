package fr.leblanc.cryptotrader.batch.writer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

public class CryptoPriceFlatFileWriter implements ItemWriter<CryptoPrice>, StepExecutionListener {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yy");

	private static final String OUTPUT_FILE = "src/main/resources/%s,%sd.json";
	
	private static final Logger logger = LoggerFactory.getLogger(CryptoPriceFlatFileWriter.class);

	private JSONArray cryptoPricesJsonArray = new JSONArray();

	@Override
	public void write(Chunk<? extends CryptoPrice> chunk) throws Exception {
		for (CryptoPrice cryptoPrice : chunk.getItems()) {
			JSONObject cryptoPriceJson = new JSONObject();
			cryptoPriceJson.put("date", cryptoPrice.getDate());
			cryptoPriceJson.put("price", cryptoPrice.getPrice());
			cryptoPricesJsonArray.put(cryptoPriceJson);
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		String days = stepExecution.getJobParameters().getString("days");
		JSONObject cryptoPriceData = new JSONObject();
		cryptoPriceData.put("date", System.currentTimeMillis());
		cryptoPriceData.put("cryptoPriceArray", cryptoPricesJsonArray);
		LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DATE_FORMATTER);
		String fileName = String.format(OUTPUT_FILE, formattedDate, days);
		ResourceUtils.storeJSON(cryptoPriceData, fileName);
		logger.info("'{}' successfully created", fileName);
		return ExitStatus.COMPLETED;
	}

}
