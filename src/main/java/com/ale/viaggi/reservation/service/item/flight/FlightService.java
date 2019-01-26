package com.ale.viaggi.reservation.service.item.flight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ale.viaggi.reservation.dao.item.flight.FlightDAO;
import com.ale.viaggi.reservation.entity.item.flight.Flight;
import com.ale.viaggi.reservation.exception.ResourceNotFoundException;

@Service
public class FlightService {

	@Autowired
	FlightDAO flightDAO;

	public boolean testService() {
		return true;
	}

	public Flight getFlightById(Long flightId) {
		Flight flightEntity = flightDAO.findById(flightId)
				.orElseThrow(() -> new ResourceNotFoundException("Flight", "flightId", flightId));

		// entity to dto
		// ModelMapper modelMapper = new ModelMapper();
		// Reservation reservation = modelMapper.map(reservationEntity,
		// Reservation.class);

		return flightEntity;
	}

}
