package fr.leblanc.cryptotrader.model;

public class CryptoTracker {
	
	private boolean isActive = true;
	
	private Double optimum = 0d;
	
	private Double gain = 0d;
	
	private final Double initialPrice;
	
	private Double currentPrice;
	
	private Double activity = 0d;
	
	private int iterations = 0;
	private int activeIterations = 0;
	
	public CryptoTracker(Double price) {
		this.initialPrice = price;
		this.currentPrice = price;
	}
	
	public Double getActivity() {
		return activity;
	}

	public Double getGain() {
		return gain;
	}
	
	public void addGain(Double gain) {
		this.gain += gain * 100 / initialPrice;
	}
	
	public Double getCurrentPrice() {
		return currentPrice;
	}
	
	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
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
	
	public Double getInitialPrice() {
		return initialPrice;
	}

	public void addIteration() {
		this.iterations++;
		if (isActive) {
			activeIterations++;
		}
		this.activity = 100 * ((double) activeIterations / iterations);
	}

	@Override
	public String toString() {
		return "CryptoTracker [gain=" + gain + ", activity=" + activity + "]";
	}

}
