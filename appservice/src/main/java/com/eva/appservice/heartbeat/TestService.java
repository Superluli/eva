package com.eva.appservice.heartbeat;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.eva.appservice.commons.exception.NestedServerRuntimeException;
import com.eva.appservice.logging.LogMe;

@Component
public class TestService {
	
	@LogMe
	public int test(int x, int y) {

		long ts = System.currentTimeMillis();
		
		System.out.println(ts);
		
		if (ts % 2 == 0) {
			throw new NestedServerRuntimeException(
					HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong...");
		}

		return x + y + 1;
	}
}
