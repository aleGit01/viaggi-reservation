package com.ale.viaggi.reservation.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ale.viaggi.reservation.avro.generated.event.ReservationEvent;
import com.ale.viaggi.reservation.avro.generated.event.ReservationEventType;
import com.ale.viaggi.reservation.avro.schema.Schemas;
import com.ale.viaggi.reservation.avro.schema.Schemas.Topics;
import com.ale.viaggi.reservation.test.util.MicroserviceTestUtils;

public class ReservationServiceTest extends MicroserviceTestUtils {

	@BeforeClass
	public static void startKafkaCluster() {
		Schemas.configureSerdesWithSchemaRegistryUrl(CLUSTER.schemaRegistryUrl());
	}

	@After
	public void shutdown() {
		// if (rest != null) {
		// rest.stop();
		// rest.cleanLocalState();
		// }
		// if (rest2 != null) {
		// rest2.stop();
		// rest2.cleanLocalState();
		// }
	}

	@Before
	public void prepareKafkaCluster() throws Exception {
		CLUSTER.deleteTopicsAndWait(30000, Topics.RESERVATION_EVENT.name(), "reservation");
		CLUSTER.createTopic(Topics.RESERVATION_EVENT.name());
		Schemas.configureSerdesWithSchemaRegistryUrl(CLUSTER.schemaRegistryUrl());
	}

	@Test
	public void testOne() throws InterruptedException {

		ReservationService service = new ReservationService();
		service.start(CLUSTER.bootstrapServers());

		ReservationEvent reservation = new ReservationEvent();
		reservation.setReservationId(1L);
		reservation.setEventType(ReservationEventType.UPDATED);

		KeyValue<String, ReservationEvent> record = new KeyValue<>(String.valueOf(reservation.getReservationId()),
				reservation);

		// Simulate the order being validated
		send(Topics.RESERVATION_EVENT, record);

		// MicroserviceTestUtils.read(Topics.RESERVATION, 2,
		// CLUSTER.bootstrapServers());
		// assertThat(MicroserviceTestUtils.read(Topics.RESERVATION, 2,
		// CLUSTER.bootstrapServers()))
		// .isEqualTo(asList(
		// new OrderValidation(id(0L), ORDER_DETAILS_CHECK, PASS),
		// new OrderValidation(id(1L), ORDER_DETAILS_CHECK, PASS)
		// ));

		List<ReservationEvent> listExpected = new ArrayList<>();
		listExpected.add(reservation);

		List<ReservationEvent> reservationsResult = MicroserviceTestUtils.read(Topics.RESERVATION_EVENT, 1,
				CLUSTER.bootstrapServers());

		ReservationEvent reservationResult = reservationsResult.get(0);

		assertEquals(reservationResult.getReservationId(), new Long(1));
	}

}