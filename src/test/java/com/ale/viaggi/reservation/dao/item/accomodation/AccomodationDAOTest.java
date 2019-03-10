package com.ale.viaggi.reservation.dao.item.accomodation;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ale.viaggi.reservation.entity.item.accomodation.AccomodationEntity;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Avoid default db. I want MYSQL
public class AccomodationDAOTest {

	@Autowired
	private AccomodationDAO accomodationDAO;

	@Test
	// @Commit // avoid the rollback (by default is active)
	public void testSave() {
		// assertTrue(TestTransaction.isActive());
		AccomodationEntity accomodationEntity = new AccomodationEntity();
		accomodationEntity.setDescription("description item from test Accomodation");
		accomodationEntity.setDescrAccomodation("HOTEL MAREMONTI");

		accomodationEntity = accomodationDAO.save(accomodationEntity);

		Optional<AccomodationEntity> accomodation = accomodationDAO.findById(accomodationEntity.getId());
		if (accomodation.isPresent()) {
			System.out.println("ACCOMODATION ID: " + accomodation.get().getId() + " DESCRIPTION="
					+ accomodation.get().getDescription());
			System.out.println("ACCOMODATION ID: " + accomodation.get().getId() + " DESCR_FLIGHT="
					+ accomodation.get().getDescrAccomodation());
		}
	}

}
