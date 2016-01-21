package com.eva.appservice.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.client.RestTemplate;

import com.eva.appservice.order.Order;

public class ConsistencyTest {

	public static RestTemplate template;

	public static void main(String[] args) throws Exception{
		
		template = new RestTemplate();
		
		ExecutorService service = Executors.newFixedThreadPool(1000);
		
		for(int i = 0; i < 1000; i++){
			
			service.submit(() -> {
				Order order = new Order();
				order.setUserId("5698a035e4d00e2feedde3dc");
				order.setProductId("5698a405e4d00e2feedde3df");
				Order orderReceived = template.postForEntity("http://localhost:8080/orders", order, Order.class).getBody();
			});
		}
		
		Thread.sleep(10000);
		
		service.shutdown();
		
	}
}
