package fr.leblanc.cryptotrader.batch.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import fr.leblanc.cryptotrader.model.CryptoPrice;
import fr.leblanc.cryptotrader.repository.CryptoPriceRepository;

public class CryptoPriceDBWriter implements ItemWriter<CryptoPrice>, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(CryptoPriceDBWriter.class);

	private CryptoPriceRepository cryptoPriceRepository;
	
	public CryptoPriceDBWriter(CryptoPriceRepository cryptoPriceRepository) {
		super();
		this.cryptoPriceRepository = cryptoPriceRepository;
	}

	@Override
	public void write(Chunk<? extends CryptoPrice> chunk) throws Exception {
		for (CryptoPrice cryptoPrice : chunk.getItems()) {
			if (cryptoPriceRepository.findByDate(cryptoPrice.getDate()) == null) {
				logger.info("saving {}", cryptoPrice);
				cryptoPriceRepository.save(cryptoPrice);
			}
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return ExitStatus.COMPLETED;
	}

}
