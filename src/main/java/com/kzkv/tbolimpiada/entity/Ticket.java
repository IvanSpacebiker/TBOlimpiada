package com.kzkv.tbolimpiada.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Enumerated(EnumType.STRING)
	private TransportType transportType;
	private double price;
	private String departure;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime departureDateTime;
	@Formula("EXTRACT(EPOCH FROM departure_date_time)")
	private Long departureEpoch;
	private String arrival;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime arrivalDateTime;

}
