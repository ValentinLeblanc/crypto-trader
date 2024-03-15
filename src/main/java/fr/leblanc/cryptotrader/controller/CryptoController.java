package fr.leblanc.cryptotrader.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {
	
	@Autowired(required = false)
	private JobLauncher jobLauncher;
	
	@Autowired(required = false)
	private Job job;
	
	@Value("${batch.job.days}")
	private String batchJobDays;
	
	@GetMapping("/import")
	public void importCryptoPrices() throws Exception {
		if (jobLauncher != null && job != null) {
			JobParameters jobParameters = new JobParametersBuilder()
			        .addLong("date", System.currentTimeMillis())
			        .addString("batchJobDays", batchJobDays)
			        .toJobParameters();
			jobLauncher.run(job, jobParameters);
		}
	}
	
}
