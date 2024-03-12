package fr.leblanc.cryptotrader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptoTraderApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CryptoTraderApplication.class, args);
	}
	
	@Autowired(required = false)
	private JobLauncher jobLauncher;
	
	@Autowired(required = false)
	private Job job;

	@Override
	public void run(String... args) throws Exception {
		if (jobLauncher != null && job != null) {
			jobLauncher.run(job, new JobParameters());
		}
	}

}
