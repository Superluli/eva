package com.eva.appservice.heartbeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eva.appservice.logging.LogMe;

@RestController
public class HeartbeatController {

	@Value("${server.port}")
	private int port;

	@Autowired
	private TestService testService;
	
	@RequestMapping(value = "/heartbeat")
	@LogMe
	public String heartbeat() throws Exception {

		testService.test(1, 100);
		return "{\"status\":\"alive\",\"port\":\"" + port + "\"}";
	}

}
