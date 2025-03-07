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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
	private final TicketRepository ticketRepository;

	public Page<Route> findAllRoutes(TicketFilters filters, Pageable pageable) {
		List<Route> allRoutes = new ArrayList<>();
		if (isInvalidFilters(filters)) {
			allRoutes =  ticketRepository.findAll(TicketSpecification.withFilters(filters))
					.stream()
					.map(ticket -> new Route(List.of(ticket)))
					.toList();
		} else {
			findRoutesIterative(filters.departure(), filters.arrival(), filters.transportType(), filters.desiredDateTime(), allRoutes);
		}

		List<Route> sortedRoutes = allRoutes
				.stream()
				.sorted((o1, o2) -> sortByDesiredTime(filters, o1, o2))
				.toList();

		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), sortedRoutes.size());

		List<Route> paginatedRoutes = sortedRoutes.subList(start, end);

		return new PageImpl<>(paginatedRoutes, pageable, sortedRoutes.size());
	}

	private int sortByDesiredTime(TicketFilters filters, Route o1, Route o2) {
		ZonedDateTime desired = filters.desiredDateTime() != null ? filters.desiredDateTime() : ZonedDateTime.now();
		long o1ToDesired = Math.abs(o1.getDepartureDateTime().toEpochSecond() - desired.toEpochSecond());
		long o2ToDesired = Math.abs(o2.getDepartureDateTime().toEpochSecond() - desired.toEpochSecond());
		return o1ToDesired > o2ToDesired ? 1 : -1;
	}

	private void findRoutesIterative(
			String start,
			String destination,
			TransportType transportType,
			ZonedDateTime desiredDateTime,
			List<Route> allRoutes
	) {
		Deque<SearchState> stack = new ArrayDeque<>();

		stack.push(new SearchState(start, new ArrayList<>(), null, new HashSet<>()));

		while (!stack.isEmpty()) {
			SearchState currentState = stack.pop();
			String current = currentState.getCurrent();
			List<Ticket> path = currentState.getPath();
			ZonedDateTime currentArrivalTime = currentState.getArrivalTime();
			Set<String> visited = currentState.getVisited();

			if (current.toLowerCase().contains(destination.toLowerCase())) {
				allRoutes.add(new Route(new ArrayList<>(path)));
				continue;
			}

			if (visited.contains(current)) continue;
			visited.add(current);

			ticketRepository.findAll(TicketSpecification.withFilters(
					new TicketFilters(current, null, transportType, desiredDateTime, currentArrivalTime)
			)).forEach(ticket -> {
				String next = ticket.getArrival();
				if (!visited.contains(next)) {
					List<Ticket> newPath = new ArrayList<>(path);
					newPath.add(ticket);
					Set<String> newVisited = new HashSet<>(visited);
					stack.push(new SearchState(next, newPath, ticket.getArrivalDateTime(), newVisited));
				}
			});
		}
	}

	private boolean isInvalidFilters(TicketFilters filters) {
		return StringUtils.isBlank(filters.departure()) || StringUtils.isBlank(filters.arrival());
	}

	public List<String> getUniqueDepartures() {
		return ticketRepository.findUniqueDepartures();
	}

	public List<String> getUniqueArrivals() {
		return ticketRepository.findUniqueArrivals();
	}
}
