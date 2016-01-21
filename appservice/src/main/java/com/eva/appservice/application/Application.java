package com.eva.appservice.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;

import com.mongodb.MongoClient;

@SpringBootApplication
@ComponentScan("com.eva.appservice")
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Value("${mongo.host}")
	private String host;

	@Value("${mongo.port}")
	private int port;

	@Value("${mongo.dbName}")
	private String dbName;

	@Bean
	public MongoTemplate mongoTemplate() throws Exception{
		
		MongoTemplate template = new MongoTemplate(new SimpleMongoDbFactory(
				new MongoClient(host, port), dbName));

		template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
		return template;
	}
}
