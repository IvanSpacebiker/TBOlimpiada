package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.entity.Ticket;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TicketGraphService {
	Map<UUID, List<Ticket>> getTicketGraph();

	void buildTicketGraph(List<Ticket> tickets);
}
