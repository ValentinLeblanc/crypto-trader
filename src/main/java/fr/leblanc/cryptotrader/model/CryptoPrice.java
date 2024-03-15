package fr.leblanc.cryptotrader.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class CryptoPrice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Let the database generate the primary key
	private Long id;
	
    private Long date;
    
    private Double price;

    public CryptoPrice() {}

    public CryptoPrice(Long date, Double price) {
        this.date = date;
        this.price = price;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

	@Override
	public int hashCode() {
		return Objects.hash(date, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CryptoPrice other = (CryptoPrice) obj;
		return Objects.equals(date, other.date) && Objects.equals(price, other.price);
	}

	@Override
	public String toString() {
		return "CryptoPrice [date=" + date + ", price=" + price + "]";
	}
    
}