package com.eva.appservice.application;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.eva.appservice.commons.Constants;
import com.eva.appservice.commons.exception.NestedServerRuntimeException;
import com.eva.appservice.logging.LoggingService;

@ControllerAdvice("com.eva.appservice")
public class AppServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private LoggingService loggingService;

	@ExceptionHandler(NestedServerRuntimeException.class)
	@ResponseBody
	ResponseEntity<?> handleControllerException(HttpServletRequest request,
			NestedServerRuntimeException ex) {

		loggingService.logException(request.getAttribute(Constants.LOGGING_CORRELATIONID).toString(), ex);

		ErrorView errorView = new ErrorView(ex.getMessage());
		return new ResponseEntity<ErrorView>(errorView, ex.getStatus());
	}

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	ResponseEntity<?> handleControllerException(HttpServletRequest request,
			Throwable ex) {

		loggingService.logException(request.getAttribute(Constants.LOGGING_CORRELATIONID).toString(), ex);

		ErrorView errorView = new ErrorView(ex.getMessage());
		return new ResponseEntity<ErrorView>(errorView, HttpStatus.INTERNAL_SERVER_ERROR);
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
