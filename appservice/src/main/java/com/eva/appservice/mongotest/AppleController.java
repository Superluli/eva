package com.eva.appservice.mongotest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eva.appservice.commons.JsonMime;

@RestController
@RequestMapping("/apples")
public class AppleController {
	
	@Autowired
	private MongoTemplate template;
	
	@JsonMime
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Apple createApple(@RequestBody Apple apple){
		

		try {
			template.insert(apple);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return apple;
	}
}
