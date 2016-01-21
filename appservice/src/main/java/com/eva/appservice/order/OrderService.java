package com.eva.appservice.order;

import org.springframework.stereotype.Service;


@Service
public interface OrderService {
	
	public Order submitOrder(Order order);
}
