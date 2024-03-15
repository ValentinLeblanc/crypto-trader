package fr.leblanc.cryptotrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.leblanc.cryptotrader.model.CryptoPrice;

public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Long> {

	CryptoPrice findByDate(Long date);

}
