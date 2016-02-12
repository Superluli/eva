package com.eva.appservice.user;

import org.springframework.data.mongodb.core.mapping.Document;

import com.eva.appservice.commons.resources.AbstractResource;
import com.eva.appservice.commons.resources.Address;

@Document(collection = "user")
public class User extends AbstractResource{

	private String firstName;
	private String lastName;
	private String email;
	private Address address;
	private int activityLevel;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getActivityLevel() {
		return activityLevel;
	}

	public void setActivityLevel(int activityLevel) {
		this.activityLevel = activityLevel;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
