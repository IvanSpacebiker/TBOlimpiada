package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.dto.Route;
import com.kzkv.tbolimpiada.dto.SearchState;
import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import com.kzkv.tbolimpiada.repository.specification.TicketSpecification;
import com.kzkv.tbolimpiada.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
	private final TicketRepository ticketRepository;

	@Cacheable(value = "routes", key = "#filters")
	public Page<Route> findAllRoutes(TicketFilters filters, Pageable pageable) {
		List<Route> sortedRoutes = getRoutes(filters)
				.stream()
				.sorted((o1, o2) -> sortByDesiredTime(filters, o1, o2))
				.toList();

		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), sortedRoutes.size());

		return new PageImpl<>(sortedRoutes.subList(start, end), pageable, sortedRoutes.size());
	}

	private List<Route> getRoutes(TicketFilters filters) {
		return isInvalidFilters(filters)
				? ticketRepository.findAll(TicketSpecification.withFilters(filters))
				.stream()
				.map(ticket -> new Route(List.of(ticket)))
				.toList()
				: findRoutesIterative(filters.departure(), filters.arrival(), filters.transportType(), filters.desiredDateTime());
	}

	private int sortByDesiredTime(TicketFilters filters, Route o1, Route o2) {
		ZonedDateTime desired = filters.desiredDateTime() != null ? filters.desiredDateTime() : ZonedDateTime.now();
		long o1ToDesired = Math.abs(o1.getDepartureDateTime().toEpochSecond() - desired.toEpochSecond());
		long o2ToDesired = Math.abs(o2.getDepartureDateTime().toEpochSecond() - desired.toEpochSecond());
		return Long.compare(o1ToDesired, o2ToDesired);
	}

	private List<Route> findRoutesIterative(
			String start,
			String destination,
			TransportType transportType,
			ZonedDateTime desiredDateTime
	) {
		List<Route> routes = new ArrayList<>();
		Deque<SearchState> stack = new ArrayDeque<>();

		stack.push(new SearchState(start, new ArrayList<>(), null, new HashSet<>()));

		while (!stack.isEmpty()) {
			SearchState currentState = stack.pop();
			String current = currentState.current();
			List<Ticket> path = currentState.path();
			ZonedDateTime currentArrivalTime = currentState.arrivalTime();
			Set<String> visited = currentState.visited();

			if (current.toLowerCase().contains(destination.toLowerCase())) {
				routes.add(new Route(new ArrayList<>(path)));
				continue;
			}

			if (visited.contains(current)) continue;

			visited.add(current);

			TicketFilters filters = new TicketFilters(current, null, transportType, desiredDateTime, currentArrivalTime);

			ticketRepository.findAll(TicketSpecification.withFilters(filters))
					.forEach(ticket -> {
						String next = ticket.getArrival();
						if (!visited.contains(next)) {
							List<Ticket> newPath = new ArrayList<>(path);
							newPath.add(ticket);
							stack.push(new SearchState(next, newPath, ticket.getArrivalDateTime(), new HashSet<>(visited)));
						}
					});
		}
		return routes;
	}

	private boolean isInvalidFilters(TicketFilters filters) {
		return StringUtils.isBlank(filters.departure()) || StringUtils.isBlank(filters.arrival()) || filters.departure().equals(filters.arrival());
	}

	@Cacheable(value = "depatures")
	public List<String> getUniqueDepartures() {
		return ticketRepository.findUniqueDepartures();
	}

	@Cacheable(value = "arrivals")
	public List<String> getUniqueArrivals() {
		return ticketRepository.findUniqueArrivals();
	}
}
