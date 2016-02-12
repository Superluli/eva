package com.eva.appservice.logging;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KafkaLogConsumer {

	@Value("${app.logging.kafka.topic}")
	private String topic;

	@Value("${app.logging.kafka.bootstrap.servers}")
	private String endpoint;

	private KafkaConsumerRunner runner;

	@PostConstruct
	public void init() {

		Properties props = new Properties();
		props.put("bootstrap.servers", endpoint);
		props.put("group.id", "consumer01");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(
				props);
		consumer.subscribe(Arrays.asList(topic));

		runner = new KafkaConsumerRunner(consumer);

		new Thread(runner).start();
	}

	@PreDestroy
	public void clear() {

		if (runner != null) {
			runner.shutdown();
		}
	}

	public static class KafkaConsumerRunner implements Runnable {

		private final AtomicBoolean closed = new AtomicBoolean(false);
		private KafkaConsumer<String, String> consumer;

		public KafkaConsumerRunner(KafkaConsumer<String, String> consumer) {
			this.consumer = consumer;
		}

		public void run() {
			try {

				while (!closed.get()) {
					ConsumerRecords<String, String> records = consumer
							.poll(100);
					for (ConsumerRecord<String, String> record : records) {
						System.out.printf("offset = %d, key = %s, value = %s \n",
								record.offset(), record.key(), record.value());
					}
				}

			} catch (WakeupException e) {
				// Ignore exception if closing
				if (!closed.get())
					throw e;

			} finally {
				consumer.close();
			}
		}

		// Shutdown hook which can be called from a separate thread
		public void shutdown() {
			closed.set(true);
			consumer.wakeup();
		}
	}
}
