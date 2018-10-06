package com.ale.viaggi.reservation.service;

import static com.ale.viaggi.reservation.avro.schema.Schemas.Topics.RESERVATION_EVENT;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ale.viaggi.reservation.avro.generated.event.ReservationEvent;
import com.ale.viaggi.reservation.avro.schema.Schemas;
import com.ale.viaggi.reservation.kafka.KafkaConstants;
import com.ale.viaggi.reservation.kafka.util.MicroserviceUtils;

@Service
public class AdviceService implements ServiceStream {

	private static final Logger log = LoggerFactory.getLogger(AdviceService.class);
	private KafkaStreams streams;
	private KafkaConsumer<String, ReservationEvent> consumer;

	@PostConstruct
	public void runStream() {
		Schemas.configureSerdesWithSchemaRegistryUrl(KafkaConstants.SCHEMA_REGISTRY_URL);
		start(KafkaConstants.KAFKA_SERVERS);
	}

	@Override
	public void log() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(String kafkaServer) {
		streams = processStreams(kafkaServer);
		// streams.cleanUp(); // don't do this in prod as it clears your state stores
		streams.start();

		log.info("Started Microservice " + "Advice");
	}

	private KafkaStreams processStreams(final String bootstrapServers) {

		StreamsBuilder builder = new StreamsBuilder();

		final KStream<String, ReservationEvent> reservations = builder.stream(RESERVATION_EVENT.name(),
				Consumed.with(RESERVATION_EVENT.keySerde(), RESERVATION_EVENT.valueSerde()));

		reservations.foreach((k, v) -> test(k, v));

		Properties props = MicroserviceUtils.baseStreamsConfig(bootstrapServers, "viaggi-reservation");
		props.setProperty(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

		return new KafkaStreams(builder.build(), props);
	}

	private void test(String k, ReservationEvent v) {
		// System.out.println("Reservation========= " + k + " event = " +
		// v.getDescription());
		log.info("Reservation========= " + k + " event = " + v.getDescription());
	}

	@Override
	public void stop() {
		if (streams != null) {
			streams.close();
		}

	}

}
