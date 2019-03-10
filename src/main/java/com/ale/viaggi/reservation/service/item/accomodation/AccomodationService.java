package com.ale.viaggi.reservation.service.item.accomodation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ale.viaggi.reservation.dao.item.accomodation.AccomodationDAO;
import com.ale.viaggi.reservation.domain.accomodation.Accomodation;
import com.ale.viaggi.reservation.entity.item.accomodation.AccomodationEntity;
import com.ale.viaggi.reservation.exception.ResourceNotFoundException;

@Service
public class AccomodationService {

	@Autowired
	AccomodationDAO accomodationDAO;

	public Accomodation getAccomodationById(Long accomodationId) {
		AccomodationEntity accomodationEntity = accomodationDAO.findById(accomodationId)
				.orElseThrow(() -> new ResourceNotFoundException("Accomodation", "accomodationId", accomodationId));

		// entity to dto
		ModelMapper modelMapper = new ModelMapper();
		Accomodation accomodation = modelMapper.map(accomodationEntity, Accomodation.class);

		return accomodation;
	}

	public Accomodation saveAccomodation(Accomodation accomodation) {

		// dto to entity
		ModelMapper modelMapper = new ModelMapper();
		AccomodationEntity accomodationEntity = modelMapper.map(accomodation, AccomodationEntity.class);

		accomodationEntity = accomodationDAO.save(accomodationEntity);

		// entity to dto
		ModelMapper modelMapper2 = new ModelMapper();
		accomodation = modelMapper.map(accomodationEntity, Accomodation.class);

		return accomodation;

	}

}
