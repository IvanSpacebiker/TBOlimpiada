package com.kzkv.tbolimpiada.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Formula;
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

	@NonNull
	@Enumerated(EnumType.STRING)
	private TransportType transportType;

	private double price;

	@NonNull
	private String departure;

	@NonNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime departureDateTime;

	@Formula("EXTRACT(EPOCH FROM departure_date_time)")
	private Long departureEpoch;

	@NonNull
	private String arrival;

	@NonNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime arrivalDateTime;

}
