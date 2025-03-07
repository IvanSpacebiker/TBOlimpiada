package com.kzkv.tbolimpiada.controller;

import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.dto.Route;
import com.kzkv.tbolimpiada.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("routes")
@RequiredArgsConstructor
@Slf4j
public class RouteController {

	private final RouteService routeService;

	@GetMapping
	public ResponseEntity<Page<Route>> getAllRoutes(
			@ModelAttribute TicketFilters filters,
			@PageableDefault Pageable pageable
	) {
		return ResponseEntity.ok(routeService.findAllRoutes(filters, pageable));
	}

	@GetMapping("arrivals")
	public ResponseEntity<List<String>> getUniqueArrivals() {
		return ResponseEntity.ok(routeService.getUniqueArrivals());
	}

	@GetMapping("departures")
	public ResponseEntity<List<String>> getUniqueDepartures() {
		return ResponseEntity.ok(routeService.getUniqueDepartures());
	}

}
