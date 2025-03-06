package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import com.kzkv.tbolimpiada.repository.specification.TicketSpecification;
import com.kzkv.tbolimpiada.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
	private final TicketRepository ticketRepository;

	public Ticket createTicket(Ticket ticket) {
		return ticketRepository.save(ticket);
	}

	public Optional<Ticket> getTickets(UUID id) {
		return ticketRepository.findById(id);
	}

	public List<Ticket> getTickets(TicketFilters filters) {
		return ticketRepository.findAll(TicketSpecification.withFilters(filters));
	}

	public Ticket updateTicket(UUID id, Ticket ticket) {
		if (!ticketRepository.existsById(id)) {
			throw new IllegalArgumentException("Route with ID " + id + " not found.");
		}
		ticket.setId(id);
		return ticketRepository.save(ticket);
	}

	public void deleteTicket(UUID id) {
		if (!ticketRepository.existsById(id)) {
			throw new IllegalArgumentException("Route with ID " + id + " not found.");
		}
		ticketRepository.deleteById(id);
	}

}

