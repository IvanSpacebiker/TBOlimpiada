package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import com.kzkv.tbolimpiada.repository.specification.TicketSpecification;
import com.kzkv.tbolimpiada.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
	private final TicketRepository ticketRepository;

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "tickets", key = "{#filters.departure(), #filters.arrival(), #filters.transportType()}")
	public List<Ticket> getAllTickets(TicketFilters filters) {
		return ticketRepository.findAll(TicketSpecification.withFilters(filters));
	}
}
