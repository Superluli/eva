package com.eva.appservice.order;

import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.eva.appservice.commons.exception.NestedServerRuntimeException;
import com.eva.appservice.logging.EventType;
import com.eva.appservice.logging.LoggingService;
import com.eva.appservice.product.Product;
import com.eva.appservice.product.ProductService;
import com.eva.appservice.user.User;
import com.eva.appservice.user.UserService;
import com.mongodb.WriteResult;

@Service
public class OrderService {
	
	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private MongoTemplate template;

	@Autowired
	private LoggingService loggingService;
	
	private static Random RAN = new Random();
	
	@PostConstruct
	public void init(){
		
	}
	
	public Order submitOrder(Order order, String sessionId) throws NestedServerRuntimeException {
		
		User user = null;
		Product product = null;

		try {
			user = userService.getById(order.getUserId());
			product = productService.getById(order.getProductId());
		} catch (Exception e) {
			throw new NestedServerRuntimeException(HttpStatus.BAD_REQUEST,
					"user or product not found", e);
		}

		boolean stockUpdated = false;

		while (!stockUpdated) {

			product = productService.getById(order.getProductId());

			int stock = product.getStockCount();

			// check if sold out
			if (stock == 0) {
				throw new NestedServerRuntimeException(HttpStatus.BAD_REQUEST,
						"product " + product + "SOLD OUT!");
			}
			WriteResult r = template.updateFirst(
					new Query(Criteria.where("_id").is(product.getId())
							.and("stockCount").is(stock)),
					new Update().inc("stockCount", -1), Product.class);

			if (r.getN() == 1) {
				stockUpdated = true;
				break;
			}
			try {
				Thread.sleep(RAN.nextInt(3000));
			} catch (InterruptedException e) {
				throw new NestedServerRuntimeException(
						HttpStatus.INTERNAL_SERVER_ERROR,
						"thread sleep interrupted", e);
			}
		}

		order.setTimestamp(System.currentTimeMillis());
		order.setAddress(user.getAddress());

		template.insert(order);
		
		loggingService.logEvent(EventType.APPSERVICE_MAKE_ORDER, sessionId, user, product, order);
		
		return order;
	}
}
