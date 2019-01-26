package com.ale.viaggi.reservation.service.item.flight;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ale.viaggi.reservation.entity.item.flight.Flight;

@RunWith(SpringRunner.class)
@SpringBootTest // Load the local SpringBootContext
public class FlightServiceTest {

	@Autowired
	FlightService flightService;

	@Test
	public void testOne() {
		flightService.testService();
	}

	@Test
	public void testGetFlightById() {
		Flight flight = flightService.getFlightById(6L);
		System.out.println("FLIGHT SERVICE: ID" + flight.getId() + " DESCR_FLIGHT=" + flight.getDescrFlight());

	}

}
