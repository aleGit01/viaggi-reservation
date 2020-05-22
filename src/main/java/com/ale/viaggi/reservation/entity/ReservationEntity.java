package com.ale.viaggi.reservation.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "reservation")
@EntityListeners(AuditingEntityListener.class)
public class ReservationEntity implements Serializable{
	
	private static final long serialVersionUID = -7421638592356762529L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="reservation_number")
	private Long reservationNumber;
	
	@Column
	private String description;
	
	
	public Long getReservationNumber() {
		return reservationNumber;
	}

	public void setReservationNumber(Long reservationNumber) {
		this.reservationNumber = reservationNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	


}
