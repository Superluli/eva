package com.eva.appservice.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eva.appservice.commons.JsonMime;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	@RequestMapping("/{id}")
	@JsonMime
	public Product getById(@PathVariable("id") String id) {

		return productService.getById(id);
	}
	
	@RequestMapping("")
	@JsonMime
	public List<Product> getByText(@RequestParam("text") String text) {

		return productService.getByText(text);
	}
}
