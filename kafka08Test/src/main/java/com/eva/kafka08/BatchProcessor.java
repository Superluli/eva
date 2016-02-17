package com.eva.kafka08;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class BatchProcessor {
	
	public void process(List<byte[]> messages){
		
		for(byte[] message : messages){
			System.out.println("processed : " + new String(message));
		}
	}
}
