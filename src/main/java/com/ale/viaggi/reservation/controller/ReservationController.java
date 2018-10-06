package com.ale.viaggi.reservation.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ale.viaggi.reservation.dto.Reservation;
import com.ale.viaggi.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	ReservationService reservationService;

	@GetMapping("/reservations")
	public List<Reservation> getAllReservations() {
		return reservationService.getAllReservations();
	}

	// Get a Single Reservation
	@GetMapping("/reservations/{id}")
	public Reservation getReservationById(@PathVariable(value = "id") Long reservationId) {
		return reservationService.getReservationById(reservationId);
	}

	// Update a Reservation
	@PutMapping("/reservations/{id}")
	public Reservation updateReservation(@PathVariable(value = "id") Long reservationId,
			@Valid @RequestBody Reservation reservationDetails) {
		return reservationService.updateReservation(reservationId, reservationDetails);
	}

	// Delete a Reservation
	@DeleteMapping("/reservations/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteReservation(@PathVariable(value = "id") Long reservationId) {
		reservationService.deleteReservation(reservationId);
	}

}
