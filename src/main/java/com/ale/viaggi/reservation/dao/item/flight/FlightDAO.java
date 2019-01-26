package com.ale.viaggi.reservation.dao.item.flight;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ale.viaggi.reservation.entity.item.flight.Flight;

public interface FlightDAO extends JpaRepository<Flight, Long> {

}
