package com.eva.jmeterbeanshell.weighted;

import java.util.List;
import java.util.Random;

public class WeightedRandomItemSelector<T extends WeightedItem> {

	private List<T> items;

	private int totalWeight;

	private static final Random RND = new Random();

	public WeightedRandomItemSelector(List<T> items) {

		this.items = items;
		totalWeight = items.stream().mapToInt(item -> item.getWeight()).sum();
	}

	public T getRandomItem() {

		double random = RND.nextDouble() * totalWeight;

		for (int i = 0; i < items.size(); i++) {

			T item = items.get(i);
			random -= item.getWeight();
			if (random <= 0.0d) {
				return item;
			}
		}
		return null;
	}
}
