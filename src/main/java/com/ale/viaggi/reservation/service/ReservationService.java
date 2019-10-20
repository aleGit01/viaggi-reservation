package com.ale.viaggi.reservation.service;

import static com.ale.viaggi.kafka.schemas.Schemas.Topics.BOOKING_EVENT;
import static com.ale.viaggi.kafka.schemas.Schemas.Topics.RESERVATION_EVENT;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ale.viaggi.event.avro.booking.BookingEvent;
import com.ale.viaggi.event.avro.reservation.ReservationEvent;
import com.ale.viaggi.event.avro.reservation.ReservationEventType;
import com.ale.viaggi.kafka.KafkaConstants;
import com.ale.viaggi.kafka.schemas.Schemas;
import com.ale.viaggi.kafka.util.MicroserviceUtils;
import com.ale.viaggi.reservation.dao.reservation.ReservationDAO;
import com.ale.viaggi.reservation.domain.reservation.Reservation;
import com.ale.viaggi.reservation.exception.ResourceNotFoundException;

@Service
public class ReservationService implements ServiceStream {
	private static final Logger logger = LogManager.getLogger(ReservationService.class);

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

	// Get a Single Reservation
	public Reservation getReservationByReservationNumber(Long reservationNumber) {
		com.ale.viaggi.reservation.entity.Reservation reservationEntity = reservationRepository
				.findByReservationNumber(reservationNumber)//
				.orElseThrow(
						() -> new ResourceNotFoundException("Reservation", "reservationNumber", reservationNumber));

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

		streams = processStreams(kafkaServer);
		// streams.cleanUp(); // don't do this in prod as it clears your state stores
		streams.start();

		// log.info("Started Microservice " + "Advice");

	}

	private KafkaStreams processStreams(final String bootstrapServers) {

		StreamsBuilder builder = new StreamsBuilder();

		final KStream<String, ReservationEvent> reservations = builder.stream(RESERVATION_EVENT.name(),
				Consumed.with(RESERVATION_EVENT.keySerde(), RESERVATION_EVENT.valueSerde()));

		// reservations.foreach((k, v) -> test(k, v));

		// Process BookingEvents
		final KStream<String, BookingEvent> bookings = builder.stream(BOOKING_EVENT.name(),
				Consumed.with(BOOKING_EVENT.keySerde(), BOOKING_EVENT.valueSerde()));

		bookings.foreach((k, v) -> processBookingEvent(k, v));

		Properties props = MicroserviceUtils.baseStreamsConfig(bootstrapServers, "viaggi-reservation");
		props.setProperty(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

		return new KafkaStreams(builder.build(), props);
	}

	@Override
	public void stop() {
		if (streams != null) {
			streams.close();
		}
	}

	private void processBookingEvent(String topic, BookingEvent bookingEvent) {
		switch (bookingEvent.getEventType()) {
		case BOOKING:
			processBooking(bookingEvent);
			break;
		case QUOTE:
			processQuote(bookingEvent);
			break;
		}

	}

	private void processQuote(BookingEvent bookingEvent) {
		// TODO Auto-generated method stub

	}

	private void processBooking(BookingEvent bookingEvent) {
		// Reservation reservation =
		// getReservationById(bookingEvent.getReservationId());
		logger.debug(": debug processBooking....Reservation=" + bookingEvent.getReservationId());
		logger.debug(": debug processBooking....Decription=" + bookingEvent.getDescription());
		logger.info(": info processBooking....Reservation=" + bookingEvent.getReservationId());
		logger.info(": info processBooking....Reservation=" + bookingEvent.getReservationId());
	}

}
