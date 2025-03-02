package com.kzkv.tbolimpiada.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private TransportType transportType;
	private double price;
	private String departure;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime departureDateTime;
	private String arrival;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime arrivalDateTime;
	@OneToOne
	private Booking booking;

}
