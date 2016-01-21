package com.eva.appservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eva.appservice.commons.JsonMime;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/{id}")
	@JsonMime
	public User getById(@PathVariable("id") String id){
		
		return userService.getById(id);
	}
}
