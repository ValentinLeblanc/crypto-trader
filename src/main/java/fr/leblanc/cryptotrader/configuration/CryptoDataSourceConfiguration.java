package fr.leblanc.cryptotrader.configuration;

import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "fr.leblanc.cryptotrader.repository", entityManagerFactoryRef = "jpaEntityManagerFactory", transactionManagerRef = "jpaTransactionManager")
public class CryptoDataSourceConfiguration {

	@Bean
	@ConfigurationProperties("spring.datasource.crypto")
	public DataSourceProperties cryptoDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource cryptoDataSource() {
		return cryptoDataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory(DataSource cryptoDataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(cryptoDataSource);
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan("fr.leblanc.cryptotrader.model");
        factory.setJpaPropertyMap(Map.of("hibernate.hbm2ddl.auto", "update"));
        return factory;
	}

	@Bean
	public PlatformTransactionManager jpaTransactionManager(LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory) {
		return new JpaTransactionManager(Objects.requireNonNull(jpaEntityManagerFactory.getObject()));
	}

}
