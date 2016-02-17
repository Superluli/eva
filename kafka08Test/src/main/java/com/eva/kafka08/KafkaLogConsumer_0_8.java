package com.eva.kafka08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

@Component
public class KafkaLogConsumer_0_8 {

	private static String topic = "test08";
	private static String consumerGroup = "whatever001";
	private static String zk = "localhost:9999";
	private static int numThreads = 3;
	
	@PostConstruct
	public void init(){
		Properties props = new Properties();
		props.put("zookeeper.connect", zk);
		props.put("group.id", consumerGroup);
		props.put("zookeeper.session.timeout.ms", "1000");
		props.put("zookeeper.sync.time.ms", "1000");
		props.put("auto.commit.enable", "false");
		props.put("auto.commit.interval.ms", "1000");

		KafkaConsumerRunner runner = new KafkaConsumerRunner(props, topic, numThreads);

		new Thread(runner).start();		
	}

	public static void main(String[] args) {
		new KafkaLogConsumer_0_8().init();
	}
	
	public static class KafkaConsumerRunner implements Runnable {

		private Properties props;
		private String topic;
		private int numThreads;
		
		private ExecutorService executor;

		public KafkaConsumerRunner(Properties props, String topic, int numThreads) {

			this.props = props;
			this.topic = topic;
			this.numThreads = numThreads;
			this.executor = Executors.newFixedThreadPool(numThreads);
		}

		@Override
		public void run() {

			Map<String, Integer> topicCountMap = new HashMap<String, Integer>();

			topicCountMap.put(topic, new Integer(numThreads));

			ConsumerConnector consumerConnector = Consumer
					.createJavaConsumerConnector(new ConsumerConfig(props));
			
			Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
					.createMessageStreams(topicCountMap);
			List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

			// now create an object to consume the messages
			//
			int threadNumber = 0;
			for (final KafkaStream<byte[], byte[]> stream : streams) {
				executor.submit(new StreamProcessor(consumerConnector, stream, threadNumber));
				threadNumber++;
			}
		}
	}

	public static class StreamProcessor implements Runnable {
		
		private ConsumerConnector consumerConnector;
		private KafkaStream<byte[], byte[]> stream;
		private int threadNumber;

		public StreamProcessor(ConsumerConnector consumerConnector, KafkaStream<byte[], byte[]> stream, int threadNumber) {
			this.stream = stream;
			this.threadNumber = threadNumber;
			this.consumerConnector = consumerConnector;
		}

		public void run() {
			ConsumerIterator<byte[], byte[]> it = stream.iterator();
			
			List<byte[]> batch = new ArrayList<byte[]>();
			
			while (it.hasNext()){
				
				batch.add(it.next().message());
				
				if(batch.size() == 3){
					processBatch(batch);
					batch.clear();
					consumerConnector.commitOffsets();	
				}
			}
		}
		
		public void processBatch(List<byte[]> messages){
			
			for(byte[] message : messages){
				System.out.println("processed : " + new String(message));
			}
		}
	}
}
