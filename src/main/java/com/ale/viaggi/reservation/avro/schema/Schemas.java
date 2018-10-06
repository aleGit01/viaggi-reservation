package com.ale.viaggi.reservation.avro.schema;

import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import com.ale.viaggi.reservation.avro.generated.event.ReservationEvent;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

public class Schemas {
	public static String schemaRegistryUrl = "";
	// public static SpecificAvroSerde<ReservationEvent>
	// RESERVATION_EVENT_AVRO_SERDE = new SpecificAvroSerde<>();

	public static void configureSerdesWithSchemaRegistryUrl(String url) {
		Topics.createTopics(); // wipe cached schema registry
		for (Topic topic : Topics.ALL.values()) {
			configure(topic.keySerde(), url);
			configure(topic.valueSerde(), url);
		}
		// configure(RESERVATION_EVENT_AVRO_SERDE, url);
		schemaRegistryUrl = url;
	}

	private static void configure(Serde serde, String url) {
		if (serde instanceof SpecificAvroSerde) {
			serde.configure(Collections.singletonMap(SCHEMA_REGISTRY_URL_CONFIG, url), false);
		}
	}

	public static class Topic<K, V> {

		private String name;
		private Serde<K> keySerde;
		private Serde<V> valueSerde;

		Topic(String name, Serde<K> keySerde, Serde<V> valueSerde) {
			this.name = name;
			this.keySerde = keySerde;
			this.valueSerde = valueSerde;
			Topics.ALL.put(name, this);
		}

		public Serde<K> keySerde() {
			return keySerde;
		}

		public Serde<V> valueSerde() {
			return valueSerde;
		}

		public String name() {
			return name;
		}

		public String toString() {
			return name;
		}
	}

	public static class Topics {
		@SuppressWarnings("rawtypes")
		public static Map<String, Topic> ALL = new HashMap<>();

		public static Topic<String, ReservationEvent> RESERVATION_EVENT;

		static {
			createTopics();
		}

		private static void createTopics() {

			RESERVATION_EVENT = new Topic<>("reservation", Serdes.String(),
					new SpecificAvroSerde<ReservationEvent>());

		}
	}
}
