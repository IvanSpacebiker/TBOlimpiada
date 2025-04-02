package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TicketGraph;
import com.kzkv.tbolimpiada.repository.TicketGraphRepository;
import com.kzkv.tbolimpiada.service.TicketGraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketGraphServiceImpl implements TicketGraphService {

	private final TicketGraphRepository ticketGraphRepository;

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

	@Override
	@Transactional
	@CacheEvict(value = "ticket_graph")
	public void buildTicketGraph(List<Ticket> tickets) {
		ticketGraphRepository.deleteAll();

		List<TicketGraph> graphLinks = tickets.stream()
				.flatMap(ticket -> tickets.stream()
						.filter(nextTicket -> ticket.getArrival().equals(nextTicket.getDeparture()) &&
								nextTicket.getDepartureDateTime().isAfter(ticket.getArrivalDateTime()))
						.map(nextTicket -> TicketGraph.builder().ticket(ticket).nextTicket(nextTicket).build())
				)
				.toList();

		ticketGraphRepository.saveAll(graphLinks);
	}
}
