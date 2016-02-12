package com.eva.appservice.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eva.appservice.commons.JsonMime;
import com.eva.appservice.logging.LoggingService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private LoggingService loggingService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@JsonMime
	public ResponseEntity<Product> getById(HttpServletRequest request,
			@PathVariable("id") String id) {

		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<Product>(productService.getById(id), headers,
				HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	@JsonMime
	public ResponseEntity<List<Product>> getByText(HttpServletRequest request,
			@RequestParam("text") String text) {

		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<List<Product>>(
				productService.getByText(text), headers, HttpStatus.OK);
	}
}
