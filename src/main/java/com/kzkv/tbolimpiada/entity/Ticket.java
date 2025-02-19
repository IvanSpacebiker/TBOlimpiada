package com.kzkv.tbolimpiada.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Ticket {

	@Id
	private UUID id;
	private String departure;
	private String arrival;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime departureDateTime;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime arrivalDateTime;
	private TransportType transportType;
	private double price;

}
