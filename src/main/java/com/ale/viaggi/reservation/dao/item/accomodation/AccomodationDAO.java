package com.ale.viaggi.reservation.dao.item.accomodation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ale.viaggi.reservation.entity.item.accomodation.Accomodation;

public interface AccomodationDAO extends JpaRepository<Accomodation, Long> {

}
