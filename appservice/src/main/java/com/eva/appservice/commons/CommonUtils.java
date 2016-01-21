package com.eva.appservice.commons;

import java.util.UUID;

public class CommonUtils {

	private CommonUtils() {
		
	}

	public static String generateUUID() {

		return UUID.randomUUID().toString();
	}
}
