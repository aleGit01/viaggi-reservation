package com.ale.viaggi.reservation.avro.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEvent {
	private Long reservationId;
	private String description;
	private ReservationEventType eventType;
}
