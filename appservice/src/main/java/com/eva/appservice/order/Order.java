package com.eva.appservice.order;

import org.springframework.data.mongodb.core.mapping.Document;

import com.eva.appservice.commons.resources.AbstractResource;
import com.eva.appservice.commons.resources.Address;

@Document(collection = "order")
public class Order extends AbstractResource{
	
	private String userId;
	private String productId;
	private long timestamp;
	private Address address;
	
	@Override
	public String toString() {
		return "Order [userId=" + userId + ", productId=" + productId + ", id="
				+ id + "]";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address location) {
		this.address = location;
	}  
}
