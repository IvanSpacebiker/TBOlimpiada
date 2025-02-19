package com.kzkv.tbolimpiada.controller;

import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("routes")
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;

	@GetMapping("{id}")
	public ResponseEntity<Ticket> getTicketById(@PathVariable UUID id) {
		return ticketService.getTickets(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<Ticket>> getTickets(@ModelAttribute TicketFilters filters) {
		return ResponseEntity.ok(ticketService.getTickets(filters));
	}

	@PostMapping
	public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
		Ticket created = ticketService.createTicket(ticket);
		return ResponseEntity
				.created(URI.create("/routes/" + created.getId()))
				.body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Ticket> updateTicket(@PathVariable UUID id, @RequestBody Ticket ticket) {
		try {
			return ResponseEntity.ok(ticketService.updateTicket(id, ticket));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
		try {
			ticketService.deleteTicket(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
}
