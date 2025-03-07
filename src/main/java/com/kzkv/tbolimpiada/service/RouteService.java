package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.dto.Route;
import com.kzkv.tbolimpiada.dto.TicketFilters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RouteService {
	Page<Route> findAllRoutes(TicketFilters filters, Pageable pageable);
	List<String> getUniqueDepartures();
	List<String> getUniqueArrivals();
}
