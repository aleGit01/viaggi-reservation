package com.ale.viaggi.reservation.test.util;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.test.TestUtils;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ale.viaggi.kafka.schemas.Schemas;
import com.ale.viaggi.kafka.schemas.Schemas.Topic;
import com.ale.viaggi.reservation.test.embedded.kafka.EmbeddedSingleNodeKafkaCluster;

import io.confluent.kafka.schemaregistry.rest.SchemaRegistryConfig;
import kafka.server.KafkaConfig;

public class MicroserviceTestUtils {

	private static final Logger log = LoggerFactory.getLogger(MicroserviceTestUtils.class);
	// private static List<TopicTailer> tailers = new ArrayList<>();
	private static int consumerCounter;

	@ClassRule
	public static final EmbeddedSingleNodeKafkaCluster CLUSTER = new EmbeddedSingleNodeKafkaCluster(new Properties() {

		private static final long serialVersionUID = -767025248743692615L;

		{
			// Transactions need durability so the defaults require multiple nodes.
			// For testing purposes set transactions to work with a single kafka broker.
			put(KafkaConfig.TransactionsTopicReplicationFactorProp(), "1");
			put(KafkaConfig.TransactionsTopicMinISRProp(), "1");
			put(KafkaConfig.TransactionsTopicPartitionsProp(), "1");
			put(SchemaRegistryConfig.KAFKASTORE_TIMEOUT_CONFIG, "60000");
		}
	});

	@AfterClass
	public static void stopCluster() {
		log.info("stopping cluster");
		if (CLUSTER.isRunning()) {
			CLUSTER.stop();
		}
	}

	protected static Properties producerConfig(EmbeddedSingleNodeKafkaCluster cluster) {
		Properties producerConfig = new Properties();
		producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, cluster.bootstrapServers());
		producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
		producerConfig.put(ProducerConfig.RETRIES_CONFIG, 0);
		return producerConfig;
	}

	public static <K, V> void send(Topic<K, V> topic, KeyValue<K, V> record) {
		send(topic, Collections.singletonList(record));
	}

	public static <K, V> void send(Topic<K, V> topic) {
		System.out.println("test");
	}

	public static <K, V> void send(Topic<K, V> topic, Collection<KeyValue<K, V>> stuff) {
		try (KafkaProducer<K, V> producer = new KafkaProducer<>(producerConfig(CLUSTER), topic.keySerde().serializer(),
				topic.valueSerde().serializer())) {
			for (KeyValue<K, V> order : stuff) {
				producer.send(new ProducerRecord<>(topic.name(), order.key, order.value)).get();
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static <K, V> List<V> read(Schemas.Topic<K, V> topic, int numberToRead, String bootstrapServers)
			throws InterruptedException {
		return readKeyValues(topic, numberToRead, bootstrapServers).stream().map(kv -> kv.value)
				.collect(Collectors.toList());
	}

	public static <K, V> List<K> readKeys(Schemas.Topic<K, V> topic, int numberToRead, String bootstrapServers)
			throws InterruptedException {
		return readKeyValues(topic, numberToRead, bootstrapServers).stream().map(kv -> kv.key)
				.collect(Collectors.toList());
	}

	public static <K, V> List<KeyValue<K, V>> readKeyValues(Schemas.Topic<K, V> topic, int numberToRead,
			String bootstrapServers) throws InterruptedException {
		Deserializer<K> keyDes = topic.keySerde().deserializer();
		Deserializer<V> valDes = topic.valueSerde().deserializer();
		String topicName = topic.name();
		return readKeysAndValues(numberToRead, bootstrapServers, keyDes, valDes, topicName);
	}

	private static <K, V> List<KeyValue<K, V>> readKeysAndValues(int numberToRead, String bootstrapServers,
			Deserializer<K> keyDes, Deserializer<V> valDes, String topicName) throws InterruptedException {
		Properties consumerConfig = new Properties();
		consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "Test-Reader-" + consumerCounter++);
		consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KafkaConsumer<K, V> consumer = new KafkaConsumer<>(consumerConfig, keyDes, valDes);
		consumer.subscribe(singletonList(topicName));

		List<KeyValue<K, V>> actualValues = new ArrayList<>();
		TestUtils.waitForCondition(() -> {
			ConsumerRecords<K, V> records = consumer.poll(100);
			for (ConsumerRecord<K, V> record : records) {
				actualValues.add(KeyValue.pair(record.key(), record.value()));
			}
			return actualValues.size() == numberToRead;
		}, 20000, "Timed out reading orders.");
		consumer.close();
		return actualValues;
	}
}
