package fr.leblanc.cryptotrader.configuration;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import fr.leblanc.cryptotrader.batch.reader.CryptoPriceReader;
import fr.leblanc.cryptotrader.batch.writer.CryptoPriceDBWriter;
import fr.leblanc.cryptotrader.batch.writer.CryptoPriceFlatFileWriter;
import fr.leblanc.cryptotrader.model.CryptoPrice;
import fr.leblanc.cryptotrader.repository.CryptoPriceRepository;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
@ConditionalOnProperty(name = "batch.job.enabled", havingValue = "true", matchIfMissing = true)
public class CryptoBatchConfiguration {
	
	@Bean
    public DataSource batchDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
        		.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql").build();
    }

	@Bean
	public PlatformTransactionManager batchTransactionManager() {
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
	public Job cryptoPriceJob(JobRepository jobRepository, PlatformTransactionManager batchTransactionManager, CryptoPriceRepository cryptoPriceRepository) {
		return new JobBuilder("cryptoPriceJob", jobRepository)
				.start(cryptoStep(jobRepository, batchTransactionManager, cryptoPriceRepository))
				.build();
	}
	
	@Bean
	public Step cryptoStep(JobRepository jobRepository, PlatformTransactionManager batchTransactionManager, CryptoPriceRepository cryptoPriceRepository) {
		return new StepBuilder("cryptoPriceStep", jobRepository)
				.<CryptoPrice, CryptoPrice>chunk(10, batchTransactionManager)
				.reader(cryptoPriceReader())
				.writer(cryptoPriceDBWriter(cryptoPriceRepository))
				.build();
	}
	
	@Bean
	public ItemReader<CryptoPrice> cryptoPriceReader() {
		return new CryptoPriceReader();
	}
	
	@Bean
	public ItemWriter<CryptoPrice> cryptoPriceFlatFileWriter() {
		return new CryptoPriceFlatFileWriter();
	}
	
	@Bean
	public ItemWriter<CryptoPrice> cryptoPriceDBWriter(CryptoPriceRepository cryptoPriceRepository) {
		return new CryptoPriceDBWriter(cryptoPriceRepository);
	}
	 
}
