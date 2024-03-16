package fr.leblanc.cryptotrader.service;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import fr.leblanc.cryptotrader.model.CryptoPrice;
import fr.leblanc.cryptotrader.repository.CryptoPriceRepository;

@Service
public class CryptoPriceService {

	private CryptoPriceRepository cryptoPriceRepository;
	
	private JobLauncher importCryptoPriceJobLauncher;
	
	private Job importCryptoPriceJob;
	
	public CryptoPriceService(JobLauncher importCryptoPriceJobLauncher, Job importCryptoPriceJob, CryptoPriceRepository cryptoPriceRepository) {
		super();
		this.importCryptoPriceJobLauncher = importCryptoPriceJobLauncher;
		this.importCryptoPriceJob = importCryptoPriceJob;
		this.cryptoPriceRepository = cryptoPriceRepository;
	}
	
	
	public List<CryptoPrice> findAll() {
		return cryptoPriceRepository.findAll();
	}

	public void launchImportCryptoPriceJob(String days) {
		JobParameters jobParameters = new JobParametersBuilder()
		        .addLong("date", System.currentTimeMillis())
		        .addString("days", days)
		        .toJobParameters();
		try {
			importCryptoPriceJobLauncher.run(importCryptoPriceJob, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}

	public void resetCryptoPrices() {
		cryptoPriceRepository.deleteAll();
	}
	
}
