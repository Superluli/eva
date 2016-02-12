package com.eva.jmeterbeanshell;

import com.eva.jmeterbeanshell.weighted.WeightedItem;

public class WeightedUser implements WeightedItem {

	String _id;
	int activityLevel;

	public int getWeight() {
		return activityLevel;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getActivityLevel() {
		return activityLevel;
	}

	public void setActivityLevel(int activityLevel) {
		this.activityLevel = activityLevel;
	}

	@Override
	public String toString() {
		return "WeightedUser [_id=" + _id + ", activityLevel=" + activityLevel
				+ "]";
	}
}
