package com.eva.appservice.logging;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eva.appservice.commons.CommonUtils;
import com.eva.appservice.commons.Constants;

@Component
public class LoggingFiler implements Filter {

	@Autowired
	private LoggingService loggingService;

	@Value("${app.enableServletLogging}")
	private boolean enableLogging;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if(!enableLogging){
			chain.doFilter(request, response);	
			return;
		}
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		request.setAttribute(Constants.LOGGING_CORRELATIONID, CommonUtils.generateSessionId());
		
		LoggingHttpServletRequestWrapper wrappedRequest = new LoggingHttpServletRequestWrapper(
				httpServletRequest);
		LoggingHttpServletResponseWrapper wrappedResponse = new LoggingHttpServletResponseWrapper(
				httpServletResponse);
		long beforeProcessing = System.currentTimeMillis();

		chain.doFilter(wrappedRequest, wrappedResponse);
		
		loggingService.logAccess(wrappedRequest, wrappedResponse,
				System.currentTimeMillis() - beforeProcessing);
	}

	@Override
	public void destroy() {

	}
}
