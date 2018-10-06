package com.ale.viaggi.reservation;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

//@Component("wordCountStreamConsumer")
public class WordCountStreamConsumer {

	private KafkaStreams streams;

	private final Log log = LogFactory.getLog(getClass());

	@PostConstruct
	public void runStream() {

		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "viaggi-reservation");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.2.111:9092");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, String> source = builder.stream("TextLinesTopic");

		source.foreach((k, v) -> System.out.println("TextLinesTopic=========" + k + ": " + v));

		KafkaStreams streams = new KafkaStreams(builder.build(), config);
		streams.start();
	}

	@PreDestroy
	public void closeStream() {
		streams.close();
	}
}