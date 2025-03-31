package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Ticket;

import java.util.List;

public interface TicketService {
	List<Ticket> getAllTickets(TicketFilters filters);
}
