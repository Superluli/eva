package com.eva.appservice.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eva.appservice.commons.Constants;
import com.eva.appservice.commons.resources.AbstractResource;
import com.eva.appservice.logging.kafka.KafkaLogProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

@Service
public class LoggingService {

	private static final Logger logger = LoggerFactory
			.getLogger(LoggingService.class);

	private ExecutorService executorService = Executors.newFixedThreadPool(100);

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Value("${server.port}")
	private String host;

	@Autowired
	private KafkaLogProducer kafkaLogProducer;
	
	public void logAccess(LoggingHttpServletRequestWrapper request,
			LoggingHttpServletResponseWrapper response, long elapsedTime) {

		executorService.submit(() -> {

			String correlationId = request.getAttribute(
					Constants.LOGGING_CORRELATIONID).toString();

			ObjectNode logNode = getEventNode(EventType.APPSERVICE_ACCESS,
					correlationId);
			logNode.put("elapsedTime", elapsedTime);

			ObjectNode contextNode = (ObjectNode) logNode.get("context");
			contextNode.set("request", getRequestNode(request));
			contextNode.set("response", getResponseNode(response));

			log(logNode.toString());

		});
	}

	private ObjectNode getRequestNode(LoggingHttpServletRequestWrapper request) {

		ObjectNode requestNode = MAPPER.createObjectNode();

		requestNode.set("method", new TextNode(request.getMethod()));
		requestNode.set("url", new TextNode(request.getRequestURI()));

		ObjectNode headersNode = MAPPER.createObjectNode();

		requestNode.set("headers", headersNode);

		Enumeration<String> headerNames = request.getHeaderNames();

		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				headersNode.put(headerName, request.getHeader(headerName));
			}
		}

		ObjectNode bodyNode = MAPPER.createObjectNode();

		byte[] bytes = request.getBytes();
		if (bytes != null && bytes.length != 0) {
			try {
				bodyNode = MAPPER.readValue(bytes, ObjectNode.class);
			} catch (IOException e) {
				bodyNode.put("jsonParseError", e.getMessage());
				bodyNode.put("rawMessage", new String(bytes,
						StandardCharsets.UTF_8));
			}
		}

		requestNode.set("body", bodyNode);

		return requestNode;
	}

	private ObjectNode getResponseNode(
			LoggingHttpServletResponseWrapper response) {

		ObjectNode responseNode = MAPPER.createObjectNode();

		responseNode.put("status", response.getStatus());

		ObjectNode headersNode = MAPPER.createObjectNode();
		responseNode.set("headers", headersNode);
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames != null) {
			for (String headerName : headerNames) {
				headersNode.put(headerName, response.getHeader(headerName));
			}
		}

		ObjectNode bodyNode = MAPPER.createObjectNode();

		byte[] bytes = response.getBody();
		if(bytes != null && bytes.length != 0){
			try {	
				bodyNode = MAPPER.readValue(response.getBody(), ObjectNode.class);
			} catch (IOException e) {
				bodyNode.put("jsonParseError", e.getMessage());
				bodyNode.put("rawMessage", new String(bytes,
						StandardCharsets.UTF_8));
			}	
		}
		
		responseNode.set("body", bodyNode);

		return responseNode;
	}

	public void logEvent(EventType event, String correlationId,
			AbstractResource... contextObjects) {

		executorService.submit(() -> {

			ObjectNode logNode = getEventNode(event, correlationId);
			ObjectNode contextNode = (ObjectNode) logNode.get("context");

			for (AbstractResource o : contextObjects) {
				contextNode.set(o.getClass().getName(),
						MAPPER.convertValue(o, ObjectNode.class));
			}

			log(logNode.toString());
		});

	}

	public void logException(String correlationId, Throwable e) {

		executorService.submit(() -> {

			ObjectNode logNode = getEventNode(EventType.APPSERVICE_EXCEPTION,
					correlationId);
			ObjectNode contextNode = (ObjectNode) logNode.get("context");
			contextNode.put("error", e.getMessage());
			log(logNode.toString());
		});
	}

	public ObjectNode getEventNode(EventType type, String correlationId) {

		ObjectNode logNode = MAPPER.createObjectNode();
		logNode.put("host", host);
		logNode.put("eventType", type.toString());
		logNode.put("correlationId", correlationId);
		logNode.put("timestamp", System.currentTimeMillis());
		logNode.set("context", MAPPER.createObjectNode());
		return logNode;
	}

	private void log(String log) {

		logger.info(log);
		kafkaLogProducer.log(log);
	}
}
