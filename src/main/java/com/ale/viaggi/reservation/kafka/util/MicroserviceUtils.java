// package com.ale.viaggi.reservation.kafka.util;
//
// import java.util.Properties;
//
// import org.apache.kafka.clients.producer.KafkaProducer;
// import org.apache.kafka.clients.producer.ProducerConfig;
// import org.apache.kafka.streams.StreamsConfig;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import com.ale.viaggi.reservation.avro.schema.Schemas;
//
// public class MicroserviceUtils {
//
// private static final Logger log =
// LoggerFactory.getLogger(MicroserviceUtils.class);
//
// public static final long MIN = 60 * 1000L;
//
// public static Properties baseStreamsConfig(String bootstrapServers, String
// appId) {
// Properties config = new Properties();
// config.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
// config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
// config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once");
// config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1); // commit as fast as
// possible
//
// return config;
// }
//
// public static <T> KafkaProducer startProducer(String bootstrapServers,
// Schemas.Topic<String, T> topic) {
// Properties producerConfig = new Properties();
// producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
// bootstrapServers);
// producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
// producerConfig.put(ProducerConfig.RETRIES_CONFIG,
// String.valueOf(Integer.MAX_VALUE));
// producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
//
// producerConfig.put("enable.auto.commit", "true");
// producerConfig.put("auto.commit.interval.ms", "1000");
//
// return new KafkaProducer<>(producerConfig, topic.keySerde().serializer(),
// topic.valueSerde().serializer());
// }
//
// }
