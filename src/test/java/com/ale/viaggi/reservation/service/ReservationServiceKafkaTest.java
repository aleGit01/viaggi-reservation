package com.ale.viaggi.reservation.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ale.viaggi.reservation.domain.reservation.Reservation;

@RunWith(SpringRunner.class)
@SpringBootTest // Load the local SpringBootContext
public class ReservationServiceKafkaTest {

	@Autowired
	ReservationService reservationService;

	@Test
	public void testUpdateReservation() {
		Reservation reservation = reservationService.getReservationByReservationNumber(1L);
		reservation.setDescription("testato");
		reservationService.updateReservation(1L, reservation);
	}

}
