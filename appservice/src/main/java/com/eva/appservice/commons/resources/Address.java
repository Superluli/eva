package com.eva.appservice.commons.resources;

public class Address {

	private String street;
	private String city;
	private State state;
	private String zip;
	private String lat;
	private String lon;

	public static class State {
		
		private String abbrev;
		private String full;

		public String getFull() {
			return full;
		}

		public void setFull(String full) {
			this.full = full;
		}

		public String getAbbrev() {
			return abbrev;
		}

		public void setAbbrev(String abbrev) {
			this.abbrev = abbrev;
		}
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
}
