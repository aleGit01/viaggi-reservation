package com.ale.viaggi.reservation.domain.flight;

import java.time.ZonedDateTime;

import com.ale.viaggi.flight.domain.airport.Airport;

public class Leg {

	public enum DirectionType {
		OUTBOUND, RETURN
	};

	private DirectionType directionType;

	private Airport departureAirport;

	private Airport arrivalAirport;

	private ZonedDateTime departureZonedDateTime;

	private ZonedDateTime arrivalDateTime;

	public Leg() {

	}

}
