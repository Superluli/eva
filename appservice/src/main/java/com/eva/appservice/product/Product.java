package com.eva.appservice.product;

import org.springframework.data.mongodb.core.mapping.Document;

import com.eva.appservice.commons.resources.AbstractResource;

@Document(collection = "product")
public class Product extends AbstractResource {

	private String name;
	private String description;
	private int stockCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", description=" + description
				+ ", stockCount=" + stockCount + ", id=" + id + "]";
	}

	public int getStockCount() {
		return stockCount;
	}

	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

}
