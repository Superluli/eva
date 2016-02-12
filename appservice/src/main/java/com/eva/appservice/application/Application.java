package com.eva.appservice.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

@SpringBootApplication
@ComponentScan("com.eva.appservice")
public class Application extends WebMvcConfigurerAdapter {


	@Value("${mongo.host}")
	private String host;

	@Value("${mongo.port}")
	private int port;

	@Value("${mongo.dbName}")
	private String dbName;
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {

		MongoTemplate template = new MongoTemplate(new SimpleMongoDbFactory(
				new MongoClient(host, port), dbName));

		
		template.setReadPreference(ReadPreference.primaryPreferred());
		template.setWriteConcern(WriteConcern.MAJORITY);
		
		template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
		
		return template;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		
	}
	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
