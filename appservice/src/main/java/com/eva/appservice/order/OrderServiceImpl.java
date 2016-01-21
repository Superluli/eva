package com.eva.appservice.order;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.eva.appservice.commons.exception.NestedServerRuntimeException;
import com.eva.appservice.product.Product;
import com.eva.appservice.product.ProductService;
import com.eva.appservice.user.User;
import com.eva.appservice.user.UserService;
import com.mongodb.WriteResult;

@Service
public class OrderServiceImpl implements OrderService {

	private static AtomicInteger i = new AtomicInteger(0);
	
	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private MongoTemplate template;

	private static Random RAN = new Random();
	
	@Override
	public Order submitOrder(Order order) throws NestedServerRuntimeException {

		System.out.println("Serving request : " + i.addAndGet(1));
		
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
						"product " + product.getName() + "SOLD OUT!");
			}
			WriteResult r = template.updateFirst(
					new Query(Criteria.where("_id").is(product.getId())
							.and("stockCount").is(stock)),
					new Update().inc("stockCount", -1), Product.class);

			if (r.getN() == 1) {
				stockUpdated = true;
				System.out.println("stock " + stock + " used");
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
		
		return order;
	}
}
