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
		long o1ToDesired = Math.abs(o1.getDepartureDateTime().toEpochSecond() - desired.toEpochSecond());
		long o2ToDesired = Math.abs(o2.getDepartureDateTime().toEpochSecond() - desired.toEpochSecond());
		long o1TravelTime = getTravelTime(o1);
		long o2TravelTime = getTravelTime(o2);
		int compareByDesiredTime = Long.compare(o1ToDesired, o2ToDesired);
		if (compareByDesiredTime == 0) {
			return -Long.compare(o1TravelTime, o2TravelTime);
		}
		return compareByDesiredTime;
	}

	private long getTravelTime(Route route) {
		return route.getTickets().getFirst().getDepartureDateTime().toEpochSecond() - route.getTickets().getLast().getArrivalDateTime().toEpochSecond();
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
