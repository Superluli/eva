package com.eva.appservice.logging;

import java.io.FileWriter;

public class Test {
	public static void main(String[] args) throws Exception{
		FileWriter writer = new FileWriter(
				"/Users/Lu/workspace/eva/kafka-test/sequence.txt");
		for (int i = 0; i < 5000; i++) {
			writer.write(String.valueOf(i) + "\n");
		}

	}
}
