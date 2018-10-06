package com.ale.viaggi.reservation;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class WordCountStreamProducer extends Thread {

	private final Log log = LogFactory.getLog(getClass());
	private int count = 0;

	@Override
	public void run() {

		final Producer<String, String> producer = createProducer();

		for (int i = 0; i < 10; i++) {
			final ProducerRecord<String, String> record = new ProducerRecord<>("TextLinesTopic", "idPerOggetto",
					"all streams lead to kafka " + String.valueOf(i));

			producer.send(record);

			// log.info("Message sent to topic: TextLinesTopic");
			System.out.println("Message sent to topic: TextLinesTopic");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		producer.flush();
		producer.close();

	}

	private static Producer<String, String> createProducer() {
		Properties config = new Properties();
		config.put("bootstrap.servers", "192.168.2.111:9092");
		config.put("group.id", "viaggi-reservation");
		config.put("enable.auto.commit", "true");
		config.put("auto.commit.interval.ms", "1000");
		config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		return new KafkaProducer<>(config);
	}

}
