package com.eva.appservice.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eva.appservice.commons.JsonMime;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(method = RequestMethod.POST)
	@JsonMime
	public Order post(@RequestBody Order orderInRequest) {

		return orderService.submitOrder(orderInRequest);
	}
}
