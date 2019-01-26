package com.ale.viaggi.reservation.entity.item.flight;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ale.viaggi.reservation.entity.item.Item;

@Entity
@Table(name = "flight")
@EntityListeners(AuditingEntityListener.class)
public class Flight extends Item {

	@Column
	private String descrFlight;

	public String getDescrFlight() {
		return descrFlight;
	}

	public void setDescrFlight(String descrFlight) {
		this.descrFlight = descrFlight;
	}
}
