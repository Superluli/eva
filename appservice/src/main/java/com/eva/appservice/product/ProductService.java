package com.eva.appservice.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.eva.appservice.commons.exception.NestedServerRuntimeException;

@Service
public class ProductService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public Product getById(String id) {

		Product result = mongoTemplate.findById(id, Product.class);

		if (result == null) {
			throw new NestedServerRuntimeException(HttpStatus.NOT_FOUND,
					"product " + id + " not found");
		}

		return result;
	}

	public List<Product> getByText(String text) {

		List<Product> results = mongoTemplate.find(
				TextQuery.queryText(new TextCriteria().matching(text))
						.sortByScore(), Product.class);

		return results;
	}
}
