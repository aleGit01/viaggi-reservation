package com.ale.viaggi.reservation;

import java.util.Arrays;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

//@Component("wordCountStream")
public class WordCountStream {

	private KafkaStreams streams;

	private final Log log = LogFactory.getLog(getClass());

	@PostConstruct
	public void runStream() {

		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "viaggi-reservation");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.2.111:9092");
		// config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
		// "alessio.os10000.net:9092");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, String> textLines = builder.stream("TextLinesTopic");
		KTable<String, Long> wordCounts = textLines
				.flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
				.groupBy((key, word) -> word)
				.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));
		wordCounts.toStream().to("WordsWithCountsTopic", Produced.with(Serdes.String(), Serdes.Long()));

		KafkaStreams streams = new KafkaStreams(builder.build(), config);
		streams.start();
	}

	@PreDestroy
	public void closeStream() {
		streams.close();
	}
}
