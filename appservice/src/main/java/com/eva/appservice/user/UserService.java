package com.eva.appservice.user;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eva.appservice.commons.exception.NestedServerRuntimeException;

@Service
public class UserService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public User getById(String id) {

		User user = mongoTemplate.findById(id, User.class);
		
		if(user == null){
			throw new NestedServerRuntimeException(HttpStatus.NOT_FOUND, "user " + id + " not found");
		}
		
		return user;
	}
}
