package fr.leblanc.cryptotrader.batch.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import fr.leblanc.cryptotrader.batch.reader.CryptoPriceReader;
import fr.leblanc.cryptotrader.batch.writer.CryptoPriceWriter;
import fr.leblanc.cryptotrader.model.CryptoPrice;

@Configuration
@EnableBatchProcessing
@ConditionalOnProperty(name = "batch.job.enabled", havingValue = "true", matchIfMissing = true)
public class BatchConfiguration {
	
	@Value("${batch.job.days}")
	private String batchJobDays;
	
	@Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
        		.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql").build();
    }

	@Bean
	public PlatformTransactionManager transactionManager() {
	    return new ResourcelessTransactionManager();
	}

	@Bean
	public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
		TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}
	
	@Bean
	public Job cryptoPriceJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("cryptoPriceJob", jobRepository)
				.start(cryptoStep(jobRepository, transactionManager))
				.build();
	}
	
	@Bean
	public Step cryptoStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("cryptoPriceStep", jobRepository)
				.<CryptoPrice, CryptoPrice>chunk(10, transactionManager)
				.reader(cryptoPriceReader(batchJobDays))
				.writer(cryptoPriceWriter(batchJobDays))
				.build();
	}
	
	@Bean
	public ItemReader<CryptoPrice> cryptoPriceReader(String batchJobDays) {
		return new CryptoPriceReader(batchJobDays);
	}
	
	@Bean
	public ItemWriter<CryptoPrice> cryptoPriceWriter(String batchJobDays) {
		return new CryptoPriceWriter(batchJobDays);
	}
	 
}
