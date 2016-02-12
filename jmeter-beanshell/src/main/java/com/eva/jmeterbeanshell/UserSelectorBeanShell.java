package com.eva.jmeterbeanshell;

import java.io.FileReader;
import java.util.List;

import com.eva.jmeterbeanshell.weighted.WeightedRandomItemSelector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class UserSelectorBeanShell {

	private static List<WeightedUser> users;
	private static List<WeightedProduct> products;

	private static WeightedRandomItemSelector<WeightedUser> userSelector;
	private static WeightedRandomItemSelector<WeightedProduct> productSelector;

	public static void main(String[] args) throws Exception{
		
		UserSelectorBeanShell.initUsers("/Users/Lu/workspace/eva/jmeter/all_users");
		System.out.println(UserSelectorBeanShell.showUsersInfo());
	}

	public static void initUsers(String file) throws Exception {

		Gson gson = new GsonBuilder().create();

		users = gson.fromJson(new FileReader(file),
				new TypeToken<List<WeightedUser>>() {
				}.getType());

		userSelector = new WeightedRandomItemSelector<WeightedUser>(users);
	}

	public static void initProducts(String file) throws Exception {

		Gson gson = new GsonBuilder().create();

		products = gson.fromJson(new FileReader(file),
				new TypeToken<List<WeightedProduct>>() {
				}.getType());

		productSelector = new WeightedRandomItemSelector<WeightedProduct>(
				products);
	}

	public static String selectRandomUser() {

		return userSelector.getRandomItem().get_id();
	}

	public static String selectRandomProduct() {

		return productSelector.getRandomItem().get_id();
	}

	public static String showUsersInfo() {
		return users.toString();
	}

	public static String showProductsInfo() {
		return products.toString();
	}
}
