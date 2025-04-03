package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.dto.Route;
import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import com.kzkv.tbolimpiada.service.implementation.RouteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

	@Mock
	private TicketRepository ticketRepository;

	@Mock
	private TicketService ticketService;

	@InjectMocks
	private RouteServiceImpl routeService;

	@Test
	void testFindAllRoutes() {
		TicketFilters filters = new TicketFilters("CityA", "CityB", TransportType.TRAIN, ZonedDateTime.now(), null);
		Pageable pageable = PageRequest.of(0, 10);

		List<Ticket> tickets = List.of(createSampleTicket());

		when(ticketService.buildRoutes(any(TicketFilters.class))).thenReturn(List.of(new Route(tickets)));

		Page<Route> result = routeService.findAllRoutes(filters, pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isNotEmpty();
		verify(ticketService, atLeastOnce()).buildRoutes(any(TicketFilters.class));
	}

	@Test
	void testGetUniqueDepartures() {
		when(ticketRepository.findUniqueDepartures()).thenReturn(List.of("CityA", "CityB"));

		List<String> result = routeService.getUniqueDepartures();

		assertThat(result).containsExactly("CityA", "CityB");
	}

	@Test
	void testGetUniqueArrivals() {
		when(ticketRepository.findUniqueArrivals()).thenReturn(List.of("CityC", "CityD"));

		List<String> result = routeService.getUniqueArrivals();

		assertThat(result).containsExactly("CityC", "CityD");
	}

	private Ticket createSampleTicket() {
		Ticket ticket = new Ticket();
		ticket.setId(UUID.randomUUID());
		ticket.setTransportType(TransportType.TRAIN);
		ticket.setPrice(100);
		ticket.setDeparture("CityA");
		ticket.setArrival("CityB");
		ticket.setDepartureDateTime(ZonedDateTime.now());
		ticket.setArrivalDateTime(ZonedDateTime.now().plusHours(2));
		return ticket;
	}
}