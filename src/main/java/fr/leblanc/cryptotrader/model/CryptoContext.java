package fr.leblanc.cryptotrader.model;

import java.util.ArrayList;
import java.util.List;

public class CryptoContext {
	
	private List<CryptoPrice> cryptoPriceList = new ArrayList<>();
	
	private boolean isActive = true;
	
	private Double optimum = 0d;
	
	private Double value;

	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	public List<CryptoPrice> getCryptoPriceList() {
		return cryptoPriceList;
	}

	public void setCryptoPriceList(List<CryptoPrice> cryptoPriceList) {
		this.cryptoPriceList = cryptoPriceList;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Double getOptimum() {
		return optimum;
	}

	public void setOptimum(Double optimum) {
		this.optimum = optimum;
	}

	@Override
	public String toString() {
		return "CryptoContext [isActive=" + isActive + ", optimum="	+ optimum + ", value="	+ value + "]";
	}
	
}
