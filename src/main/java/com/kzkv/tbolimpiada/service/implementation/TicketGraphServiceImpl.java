package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TicketGraph;
import com.kzkv.tbolimpiada.repository.TicketGraphRepository;
import com.kzkv.tbolimpiada.service.TicketGraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketGraphServiceImpl implements TicketGraphService {

	private final TicketGraphRepository ticketGraphRepository;

	@Override
	@Transactional
	public void buildTicketGraph(List<Ticket> tickets) {
		ticketGraphRepository.deleteAll();
		Map<String, List<Ticket>> departureMap = tickets.stream()
				.collect(Collectors.groupingBy(Ticket::getDeparture));

		List<TicketGraph> graphLinks = new ArrayList<>();

		for (Ticket ticket : tickets) {
			List<Ticket> nextTickets = departureMap.getOrDefault(ticket.getArrival(), Collections.emptyList())
					.stream()
					.filter(next -> next.getDepartureDateTime().isAfter(ticket.getArrivalDateTime()))
					.toList();

			for (Ticket nextTicket : nextTickets) {
				TicketGraph link = new TicketGraph();
				link.setTicket(ticket);
				link.setNextTicket(nextTicket);
				graphLinks.add(link);
			}
		}

		ticketGraphRepository.saveAll(graphLinks);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "ticket_graph")
	public Map<UUID, List<Ticket>> getTicketGraph() {
		return ticketGraphRepository.findAll().stream()
				.collect(Collectors.groupingBy(
						graph -> graph.getTicket().getId(),
						Collectors.mapping(TicketGraph::getNextTicket, Collectors.toList())
				));
	}
}
