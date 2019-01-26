package com.ale.viaggi.reservation.domain.flight;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Segments in air travel are defined by airlines as all travel, including all legs and stopovers, 
 * which are listed under the same flight number on an itinerary. 
 * In essence, no matter how many times a passenger boards or gets off the same aircraft, 
 * a flight segment is defined as travel from the time the passenger initially boards the plane until 
 * he disembarks at his destination, as long as it is on the same aircraft 
 * and the flight number does not change. 
 * The number of segments in your travel plans is equal to the number of flight numbers on your itinerary.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {

	private List<Segment> segments;
	private String ritorno;

	// outbound
	// return

}
