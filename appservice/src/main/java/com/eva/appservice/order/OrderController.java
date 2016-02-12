package com.eva.appservice.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eva.appservice.commons.Constants;
import com.eva.appservice.commons.JsonMime;
import com.eva.appservice.logging.LoggingService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private LoggingService loggingService;

	@RequestMapping(method = RequestMethod.POST)
	@JsonMime
	public ResponseEntity<Order> post(HttpServletRequest request,
			@RequestBody Order orderInRequest) throws Exception {

		String correlationId = request
				.getAttribute(Constants.LOGGING_CORRELATIONID).toString();

		Order order = orderService.submitOrder(orderInRequest, correlationId);

		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<Order> response = new ResponseEntity<Order>(order,
				headers, HttpStatus.OK);

		return response;
	}
}
