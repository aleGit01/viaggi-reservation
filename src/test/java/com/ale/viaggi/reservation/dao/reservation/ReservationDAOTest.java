package com.ale.viaggi.reservation.dao.reservation;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ale.viaggi.reservation.dao.item.accomodation.AccomodationDAO;
import com.ale.viaggi.reservation.entity.ReservationEntity;
import com.ale.viaggi.reservation.entity.item.flight.Flight;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Avoid default db. I want MYSQL
public class ReservationDAOTest {
	
	@Autowired
	private ReservationDAO reservationDAO;
	
	@Test
	// @Commit // avoid the rollback (by default is active)
	public void testSave() {
		// assertTrue(TestTransaction.isActive());
		com.ale.viaggi.reservation.entity.ReservationEntity reservationEntity = new ReservationEntity();
		reservationEntity.setDescription("description Reservation from test");
		reservationEntity.setReservationNumber(33L);

		reservationEntity = reservationDAO.save(reservationEntity);

		Optional<ReservationEntity> reservation = reservationDAO.findById(reservationEntity.getId());
		if (reservation.isPresent()) {
			System.out.println("Reservation ID: " + reservation.get().getId() + " DESCRIPTION=" + reservation.get().getDescription());
		}
	}

}
