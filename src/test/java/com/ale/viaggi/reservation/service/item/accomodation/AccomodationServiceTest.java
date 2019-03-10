package com.ale.viaggi.reservation.service.item.accomodation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ale.viaggi.reservation.domain.accomodation.Accomodation;

@RunWith(SpringRunner.class)
@SpringBootTest // Load the local SpringBootContext
public class AccomodationServiceTest {

	@Autowired
	AccomodationService accomodationService;

	@Test
	public void testGetAccomodationById() {
		Accomodation accomodation = accomodationService.getAccomodationById(9L);
		System.out.println("ACCOMODATION SERVICE: ID" + accomodation.getId() + " DESCR_ACCOMODATION="
				+ accomodation.getDescrAccomodation());

	}

	@Test
	public void testSaveAccomodation() {
		Accomodation accomodation = new Accomodation();
		accomodation.setDescription("description item from test Accomodation");
		accomodation.setDescrAccomodation("HOTEL TROPICANA");

		accomodation = accomodationService.saveAccomodation(accomodation);

		System.out
				.println("ACCOMODATION ID: " + accomodation.getId() + " DESCRIPTION=" + accomodation.getDescription());

	}

}