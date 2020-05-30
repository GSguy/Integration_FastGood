package acs.data;

import javax.persistence.Embeddable;

@Embeddable
public class Dish {
	
	private String name;
	private Double price;

	public Dish() {
		super();
	}

	public Dish(String name, Double price) {
		setName(name);
		setPrice(price);
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
