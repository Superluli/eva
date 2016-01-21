package com.eva.appservice.heartbeat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartbeatController {

	@Value("${server.port}")
	private int port;

	@RequestMapping(value = "/heartbeat")
	public String heartbeat() throws Exception{
		return "alive on " + port;
	}
}
