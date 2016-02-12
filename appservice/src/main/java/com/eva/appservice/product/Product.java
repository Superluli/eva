package com.eva.appservice.product;

import org.springframework.data.mongodb.core.mapping.Document;

import com.eva.appservice.commons.resources.AbstractResource;

@Document(collection = "product")
public class Product extends AbstractResource{

	private String make;
	private String model;
	private int stockCount;

	private int popularity;
	
	public int getStockCount() {
		return stockCount;
	}

	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return "Product [make=" + make + ", model=" + model + ", stockCount="
				+ stockCount + "]";
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}
}
