package com.eva.appservice.logging.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public class KafkaLogConsumer_0_9 {

	@Value("${app.logging.kafka.topic}")
	private String topic;

	@Value("${app.logging.kafka.bootstrap.servers}")
	private String endpoint;

	private KafkaConsumerRunner runner;

	private ExecutorService executor = Executors.newCachedThreadPool();

	@PostConstruct
	public void init() {

		Properties props = new Properties();
		props.put("bootstrap.servers", endpoint);
		props.put("group.id", "consumer01");
		props.put("enable.auto.commit", "false");
		props.put("auto.commit.interval.ms", "5000");
		props.put("offsets.storage", "kafka");
		props.put("dual.commit.enabled", "false");

		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer",
				"org.apache.kafka.common.serialization.StringDeserializer");

		executor.submit(new KafkaConsumerRunner(props, topic, executor));

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

		private Executor executor;
		private Properties props;
		private String topic;
		private KafkaConsumer<String, String> consumer;

		public KafkaConsumerRunner(Properties props, String topic,
				Executor executor) {

			this.props = props;
			this.topic = topic;
			this.executor = executor;
		}

		public void run() {

			while (true) {

				consumer = new KafkaConsumer<String, String>(props);
				consumer.subscribe(Arrays.asList(topic));

				try {

					while (!closed.get()) {
						ConsumerRecords<String, String> records = consumer
								.poll(5000);

						CompletionService<Void> completionService = new ExecutorCompletionService<Void>(
								executor);

						int nSubmitted = 0;

						for (ConsumerRecord<String, String> record : records) {

							try {
								completionService.submit(() -> {
									process(record);
									return null;
								});

								nSubmitted += 1;

							} catch (RejectedExecutionException e) {
								System.err.println("back up : "
										+ record.value());
							}
						}

						try {
							for (int i = 0; i < nSubmitted; i++) {
								completionService.take();
							}
						} catch (InterruptedException e) {
						}

						consumer.commitSync();
					}

				} catch (WakeupException e) {
					// Ignore exception if closing
					if (!closed.get())
						throw e;

				} finally {
					consumer.close();
				}

			}
		}

		private void process(ConsumerRecord<String, String> record) {

			if (new Random().nextBoolean()) {

				System.err.println("backup : " + record.value());
			}

			System.err.println("consumed : " + record.value());
		}

		// Shutdown hook which can be called from a separate thread
		public void shutdown() {

			closed.set(true);
			consumer.wakeup();
		}
	}
}
