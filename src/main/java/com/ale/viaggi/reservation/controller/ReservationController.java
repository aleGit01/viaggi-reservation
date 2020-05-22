package com.ale.viaggi.reservation.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ale.viaggi.reservation.domain.reservation.Reservation;
import com.ale.viaggi.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservation")
@CrossOrigin(origins = "http://localhost:4200") // Needed for Angular!!!! Configure CORS for Spring Boot
												// https://dzone.com/articles/bootiful-development-with-spring-boot-and-angular
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

	// Get a Single Reservation
//	@GetMapping("/reservations/{reservationNumber}")
//	public Reservation getReservationByReservationNumber(
//			@PathVariable(value = "reservationNumber") Long reservationNumber) {
//		return reservationService.getReservationByReservationNumber(reservationNumber);
//	}

	// Update a Reservation
	@PutMapping("/reservations/{id}")
	public Reservation updateReservation(@PathVariable(value = "id") Long reservationId,
			@Valid @RequestBody Reservation reservationDetails) {
		return reservationService.updateReservation(reservationId, reservationDetails);
	}

	// Create a Reservation
	@PostMapping("/reservations")
	public Reservation createReservation(@Valid @RequestBody Reservation reservation) {
		return reservationService.createReservation(reservation);
	}

	// Delete a Reservation
	@DeleteMapping("/reservations/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteReservation(@PathVariable(value = "id") Long reservationId) {
		reservationService.deleteReservation(reservationId);
	}

}
