package com.kzkv.tbolimpiada.repository.generator;

import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import com.kzkv.tbolimpiada.repository.BookingRepository;
import com.kzkv.tbolimpiada.repository.TicketGraphRepository;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import com.kzkv.tbolimpiada.service.TicketGraphService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class TestDataGenerator {

	private final TicketRepository ticketRepository;
	private final BookingRepository bookingRepository;
	private final TicketGraphRepository ticketGraphRepository;
	private final TicketGraphService ticketGraphService;

	@Value("${generator.amount}")
	private int numberOfTickets;

	@PostConstruct
	public void generateData() {
		ticketGraphRepository.deleteAll();
		bookingRepository.deleteAll();
		ticketRepository.deleteAll();

		List<Ticket> tickets = new LinkedList<>();

		for (int i = 0; i < numberOfTickets; i++) {
			Ticket ticket = new Ticket();
			setRandomTransportType(ticket);
			setRandomPrice(ticket);
			setRandomCities(ticket);
			setRandomDateTimes(ticket);
			tickets.add(ticket);
		}

		ticketRepository.saveAll(tickets);
		ticketGraphService.buildTicketGraph(tickets);
	}

	private void setRandomTransportType(Ticket ticket) {
		TransportType[] values = TransportType.values();
		ticket.setTransportType(values[ThreadLocalRandom.current().nextInt(values.length - 1)]);
	}

	private void setRandomPrice(Ticket ticket) {
		ticket.setPrice(100 + ThreadLocalRandom.current().nextInt(9900));
	}

	private void setRandomCities(Ticket ticket) {
		String[] cities = {
				"New York", "London", "Berlin", "Paris", "Tokyo", "Moscow", "Toronto", "Sydney", "Los Angeles",
				"Madrid", "Rome", "Minsk", "Beijing", "Prague", "Warsaw", "Saint-Petersburg", "Dublin", "Amsterdam",
				"Brussel", "Stockholm", "Helsinki", "Oslo"
		};
		int departure = ThreadLocalRandom.current().nextInt(cities.length);
		int arrival = (departure + ThreadLocalRandom.current().nextInt(1, cities.length)) % cities.length;
		ticket.setDeparture(cities[departure]);
		ticket.setArrival(cities[arrival]);
	}

	private void setRandomDateTimes(Ticket ticket) {
		ZonedDateTime startDate = ZonedDateTime.of(2025, 6, 1, 0, 0, 0, 0, ZoneId.systemDefault());
		ZonedDateTime endDate = startDate.plusYears(1);
		long startMillis = startDate.toInstant().toEpochMilli();
		long endMillis = endDate.toInstant().toEpochMilli();

		long randomMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis);

		ZonedDateTime departure = ZonedDateTime.ofInstant(Instant.ofEpochMilli(randomMillis), ZoneId.systemDefault());
		ZonedDateTime arrival = departure.plusMinutes(ThreadLocalRandom.current().nextInt(60, 720));

		ticket.setDepartureDateTime(departure);
		ticket.setArrivalDateTime(arrival);
	}
}