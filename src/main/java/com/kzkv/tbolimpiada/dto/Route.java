package com.kzkv.tbolimpiada.dto;

import com.kzkv.tbolimpiada.entity.Ticket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Route {

	public Route(List<Ticket> tickets) {
		this.tickets = new ArrayList<>(tickets);
		this.price = tickets.stream().mapToDouble(Ticket::getPrice).sum();
		this.departure = tickets.getFirst().getDeparture();
		this.departureDateTime = tickets.getFirst().getDepartureDateTime();
		this.arrival = tickets.getLast().getArrival();
		this.arrivalDateTime = tickets.getLast().getArrivalDateTime();
	}

	private List<Ticket> tickets;

	private double price;

	private String departure;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime departureDateTime;

	private String arrival;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime arrivalDateTime;

}
