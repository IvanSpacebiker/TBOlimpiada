package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.dto.Route;
import com.kzkv.tbolimpiada.dto.TicketFilters;

import java.util.List;

public interface TicketService {
	List<Route> buildRoutes(TicketFilters filters);
}
