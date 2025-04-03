package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.dto.Route;
import com.kzkv.tbolimpiada.dto.SearchState;
import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import com.kzkv.tbolimpiada.repository.specification.TicketSpecification;
import com.kzkv.tbolimpiada.service.TicketGraphService;
import com.kzkv.tbolimpiada.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
	private final TicketRepository ticketRepository;
	private final TicketGraphService ticketGraphService;

	@Value("${search.depth}")
	private int maxDepth;

	@Override
	@Cacheable(value = "routes", key = "{#filters.departure(), #filters.arrival(), #filters.transportType()}")
	@Transactional(readOnly = true)
	public List<Route> buildRoutes(TicketFilters filters) {
		return isInvalidFilters(filters)
				? buildRoutesSimple(filters)
				: buildRoutesIterative(filters.departure(), filters.arrival(), filters.transportType());
	}

	@Transactional(readOnly = true)
	protected List<Route> buildRoutesSimple(TicketFilters filters) {
		return ticketRepository.findAll(TicketSpecification.withFilters(filters))
				.stream()
				.map(ticket -> new Route(List.of(ticket)))
				.toList();
	}

	@Transactional(readOnly = true)
	protected List<Route> buildRoutesIterative(String start, String destination, TransportType transportType) {
		Map<UUID, List<Ticket>> ticketGraph = ticketGraphService.getTicketGraph();
		List<Ticket> startTickets = ticketRepository.findByDeparture(start);
		List<Route> routes = new LinkedList<>();
		Queue<SearchState> queue = new LinkedList<>();

		queue.add(new SearchState(start, new LinkedList<>(), null, new HashSet<>()));

		while (!queue.isEmpty()) {
			processState(destination, transportType, queue, routes, startTickets, ticketGraph);
		}
		return routes;
	}

	private void processState(
			String destination,
			TransportType transportType,
			Queue<SearchState> queue,
			List<Route> routes,
			List<Ticket> startTickets,
			Map<UUID, List<Ticket>> ticketGraph
	) {
		SearchState currentState = queue.poll();

		if (currentState.current().equals(destination)) {
			routes.add(new Route(new LinkedList<>(currentState.path())));
			return;
		}

		getNextTickets(currentState.path(), startTickets, ticketGraph).forEach(ticket -> searchRoutes(currentState, ticket, transportType, queue));
	}

	private void searchRoutes(SearchState currentState, Ticket ticket, TransportType transportType, Queue<SearchState> queue) {
		String nextCity = ticket.getArrival();

		if (isCorrectRoute(currentState, ticket, transportType)) {

			List<Ticket> newPath = new LinkedList<>(currentState.path());
			newPath.add(ticket);

			Set<String> newVisited = new HashSet<>(currentState.visited());
			newVisited.add(nextCity);

			queue.add(new SearchState(nextCity, newPath, ticket.getArrivalDateTime(), newVisited));
		}
	}

	private boolean isCorrectRoute(SearchState state, Ticket ticket, TransportType transportType) {
		return !state.visited().contains(ticket.getArrival())
				&& state.path().size() < maxDepth
				&& isTransportTypeCorrect(ticket, transportType)
				&& isArrivalTimeCorrect(ticket, state.arrivalTime());
	}

	private boolean isArrivalTimeCorrect(Ticket ticket, ZonedDateTime currentArrivalTime) {
		return currentArrivalTime == null || ticket.getDepartureDateTime().isAfter(currentArrivalTime);
	}

	private boolean isTransportTypeCorrect(Ticket ticket, TransportType transportType) {
		return transportType == TransportType.ANY || ticket.getTransportType() == transportType;
	}

	private List<Ticket> getNextTickets(List<Ticket> path, List<Ticket> startTickets, Map<UUID, List<Ticket>> ticketGraph) {
		if (path.isEmpty()) return startTickets;
		return ticketGraph.getOrDefault(path.getLast().getId(), Collections.emptyList());
	}


	private boolean isInvalidFilters(TicketFilters filters) {
		return StringUtils.isBlank(filters.departure()) || StringUtils.isBlank(filters.arrival()) || filters.departure().equals(filters.arrival());
	}
}
