package com.eva.appservice.logging.kafka;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eva.appservice.commons.CommonUtils;

@Service
public class KafkaLogProducer {

	private Producer<String, String> producer;

	@Value("${app.logging.kafka.topic}")
	private String topic;

	@Value("${app.logging.kafka.partition}")
	private int partition;

	@Value("${app.logging.kafka.bootstrap.servers}")
	private String endpoint;

	@PostConstruct
	public void init() {

		Properties props = new Properties();
		props.put("bootstrap.servers", endpoint);
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("default.replication.factor", 3);
		props.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");

		producer = new KafkaProducer<String, String>(props);
		
		
		System.out.println("============================" + partition);
	}

	@PreDestroy
	public void clear() {
		if (producer != null) {
			producer.close();
		}
	}

	public void log(String log) {

		producer.send(new ProducerRecord<String, String>(topic, partition,
				CommonUtils.generateUUID(), log));
	}
}
