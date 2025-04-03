package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.dto.Route;
import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import com.kzkv.tbolimpiada.service.RouteService;
import com.kzkv.tbolimpiada.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
	private final TicketRepository ticketRepository;
	private final TicketService ticketService;

	public Page<Route> findAllRoutes(TicketFilters filters, Pageable pageable) {
		List<Route> routes = ticketService.buildRoutes(filters)
				.stream()
				.sorted((o1, o2) -> sortRoutes(filters, o1, o2))
				.toList();

		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), routes.size());

		return new PageImpl<>(routes.subList(start, end), pageable, routes.size());
	}

	private int sortRoutes(TicketFilters filters, Route o1, Route o2) {
		ZonedDateTime desired = filters.desiredDateTime() != null ? filters.desiredDateTime() : ZonedDateTime.now();
		return Comparator
				.comparingLong((Route r) -> Math.abs(r.getDepartureDateTime().toEpochSecond() - desired.toEpochSecond()))
				.thenComparingLong(this::getTravelTime)
				.compare(o1, o2);
	}


	private long getTravelTime(Route route) {
		return route.getTickets().getLast().getArrivalDateTime().toEpochSecond() - route.getTickets().getFirst().getDepartureDateTime().toEpochSecond();
	}

	@Cacheable(value = "departures")
	public List<String> getUniqueDepartures() {
		return ticketRepository.findUniqueDepartures();
	}

	@Cacheable(value = "arrivals")
	public List<String> getUniqueArrivals() {
		return ticketRepository.findUniqueArrivals();
	}
}
