package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketService {

	Ticket createTicket(Ticket ticket);

	Optional<Ticket> getTickets(UUID id);

	List<Ticket> getTickets(TicketFilters filters);

	Ticket updateTicket(UUID id, Ticket ticket);

	void deleteTicket(UUID id);

}

