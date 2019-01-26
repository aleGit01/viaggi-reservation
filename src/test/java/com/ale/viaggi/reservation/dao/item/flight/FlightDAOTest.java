package com.ale.viaggi.reservation.dao.item.flight;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ale.viaggi.reservation.dao.item.flight.FlightDAO;
import com.ale.viaggi.reservation.entity.item.flight.Flight;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Avoid default db. I want MYSQL
public class FlightDAOTest {

	@Autowired
	private FlightDAO flightDAO;

	@Test
	// @Commit // avoid the rollback (by default is active)
	public void testSave() {
		// assertTrue(TestTransaction.isActive());
		com.ale.viaggi.reservation.entity.item.flight.Flight flightEntity = new Flight();
		flightEntity.setDescription("description item from test");
		flightEntity.setDescrFlight("description Flight");

		flightEntity = flightDAO.save(flightEntity);

		Optional<Flight> flight = flightDAO.findById(flightEntity.getId());
		if (flight.isPresent()) {
			System.out.println("FLIGHT ID: " + flight.get().getId() + " DESCRIPTION=" + flight.get().getDescription());
			System.out.println("FLIGHT ID: " + flight.get().getId() + " DESCR_FLIGHT=" + flight.get().getDescrFlight());
		}
	}

}
