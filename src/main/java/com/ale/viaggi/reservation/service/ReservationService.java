package com.ale.viaggi.reservation.service;

import static com.ale.viaggi.reservation.avro.schema.Schemas.Topics.RESERVATION_EVENT;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ale.viaggi.reservation.avro.generated.event.ReservationEvent;
import com.ale.viaggi.reservation.avro.generated.event.ReservationEventType;
import com.ale.viaggi.reservation.avro.schema.Schemas;
import com.ale.viaggi.reservation.dao.reservation.ReservationDAO;
import com.ale.viaggi.reservation.dto.Reservation;
import com.ale.viaggi.reservation.exception.ResourceNotFoundException;
import com.ale.viaggi.reservation.kafka.KafkaConstants;
import com.ale.viaggi.reservation.kafka.util.MicroserviceUtils;

@Service
public class ReservationService implements ServiceStream {

	@Autowired
	ReservationDAO reservationRepository;

	private KafkaProducer<String, ReservationEvent> producer;
	private KafkaStreams streams;

	@PostConstruct
	public void runStream() {
		Schemas.configureSerdesWithSchemaRegistryUrl(KafkaConstants.SCHEMA_REGISTRY_URL);
		start(KafkaConstants.KAFKA_SERVERS);
	}

	public List<Reservation> getAllReservations() {
		List<com.ale.viaggi.reservation.entity.Reservation> reservationEntities = reservationRepository.findAll();

		ModelMapper modelMapper = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<Reservation>>() {
		}.getType();
		List<Reservation> reservations = modelMapper.map(reservationEntities, targetListType);

		return reservations;
	}

	// Get a Single Reservation
	public Reservation getReservationById(Long reservationId) {
		com.ale.viaggi.reservation.entity.Reservation reservationEntity = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

		// entity to dto
		ModelMapper modelMapper = new ModelMapper();
		Reservation reservation = modelMapper.map(reservationEntity, Reservation.class);

		return reservation;
	}

	// Update a Reservation
	public Reservation updateReservation(Long reservationId, Reservation reservationDetails) {

		com.ale.viaggi.reservation.entity.Reservation reservationEntity = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

		reservationEntity.setDescription(reservationDetails.getDescription());

		reservationEntity = reservationRepository.save(reservationEntity);

		sendEvent(reservationEntity);

		Reservation reservation = new Reservation();

		// entity to dto
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.map(reservationEntity, reservation);

		return reservation;
	}

	private void sendEvent(com.ale.viaggi.reservation.entity.Reservation reservationEntity) {
		ReservationEvent reservatioEvent = fromEntity(reservationEntity);

		// Keys are mostly useful/necessary if you require strong order for a key and
		// are developing something like a state machine. If you require that messages
		// with the same key (for instance, a unique id) are always seen in the correct
		// order, attaching a key to messages will ensure messages with the same key
		// always go to the same partition in a topic. Kafka guarantees order within a
		// partition, but not across partitions in a topic, so alternatively not
		// providing a key - which will result in round-robin distribution across
		// partitions - will not maintain such order.

		String topic = RESERVATION_EVENT.name();
		String keyPartition = KafkaConstants.KEY_PARTITION_RESERVATION;
		ProducerRecord<String, ReservationEvent> producerRecord = new ProducerRecord<>(topic, keyPartition,
				reservatioEvent);

		producer.send(producerRecord);
	}

	private ReservationEvent fromEntity(com.ale.viaggi.reservation.entity.Reservation reservationEntity) {
		ReservationEvent reservationEvent = new ReservationEvent();

		reservationEvent.setEventType(ReservationEventType.UPDATED);
		reservationEvent.setDescription(reservationEntity.getDescription());
		reservationEvent.setReservationId(reservationEntity.getReservationNumber());

		return reservationEvent;
	}

	// Delete a Reservation
	public void deleteReservation(Long reservationId) {
		com.ale.viaggi.reservation.entity.Reservation reservationEntity = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

		reservationRepository.delete(reservationEntity);
	}

	@Override
	public void log() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(String kafkaServer) {

		producer = MicroserviceUtils.startProducer(kafkaServer, RESERVATION_EVENT);

		// OrdersService service = new OrdersService(restHostname, restPort == null ? 0
		// : Integer.valueOf(restPort));
		// service.start(bootstrapServers, "/tmp/kafka-streams");
		// addShutdownHookAndBlock(service);

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
