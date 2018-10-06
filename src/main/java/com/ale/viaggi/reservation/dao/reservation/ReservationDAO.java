package com.ale.viaggi.reservation.dao.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ale.viaggi.reservation.entity.Reservation;

@Repository
public interface  ReservationDAO extends JpaRepository<Reservation, Long>{

}
