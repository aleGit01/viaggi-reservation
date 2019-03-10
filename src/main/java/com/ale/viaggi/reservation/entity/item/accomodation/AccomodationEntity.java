package com.ale.viaggi.reservation.entity.item.accomodation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ale.viaggi.reservation.entity.item.Item;

@Entity
@Table(name = "accomodation")
@EntityListeners(AuditingEntityListener.class)
public class AccomodationEntity extends Item {

	@Column
	private String descrAccomodation;

	public String getDescrAccomodation() {
		return descrAccomodation;
	}

	public void setDescrAccomodation(String descrAccomodation) {
		this.descrAccomodation = descrAccomodation;
	}

}
