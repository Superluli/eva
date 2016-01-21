package com.eva.appservice.application;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.eva.appservice.commons.exception.NestedServerRuntimeException;

@ControllerAdvice("com.eva.appservice")
public class AppServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NestedServerRuntimeException.class)
	@ResponseBody
	ResponseEntity<?> handleControllerException(HttpServletRequest request,
			NestedServerRuntimeException ex) {

		//ex.printStackTrace();
		
		return new ResponseEntity<ErrorView>(new ErrorView(ex.getMessage()),
				ex.getStatus());
	}

	public static class ErrorView {

		String message;

		public ErrorView() {

		}

		ErrorView(String message) {

			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
