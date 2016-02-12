package com.eva.appservice.mongotest;

import org.springframework.data.mongodb.core.mapping.Document;

import com.eva.appservice.commons.resources.AbstractResource;

@Document(collection = "apple")
public class Apple extends AbstractResource{

	
}
