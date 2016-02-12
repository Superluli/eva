package com.eva.jmeterbeanshell;

import com.eva.jmeterbeanshell.weighted.WeightedItem;

public class WeightedProduct implements WeightedItem {

	String _id;
	int popularity;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	@Override
	public int getWeight() {
		return popularity;
	}

	@Override
	public String toString() {
		return "WeightedProduct [_id=" + _id + ", popularity=" + popularity
				+ "]";
	}
}
