package com.eva.appservice.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eva.appservice.commons.JsonMime;
import com.eva.appservice.logging.LoggingService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private LoggingService loggingService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@JsonMime
	public ResponseEntity<User> getById(HttpServletRequest request,
			@PathVariable("id") String id) {

		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<User>(
				userService.getById(id), headers, HttpStatus.OK);
	}
}
