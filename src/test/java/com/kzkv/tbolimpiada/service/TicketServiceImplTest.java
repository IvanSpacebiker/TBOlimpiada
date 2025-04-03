package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.dto.Route;
import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.TransportType;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import com.kzkv.tbolimpiada.service.implementation.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketGraphService ticketGraphService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepository, ticketGraphService);
    }

    @Test
    void testBuildRoutes_WithInvalidFilters_ReturnsSimpleRoutes() {
        TicketFilters filters = new TicketFilters("", "", TransportType.ANY, null, null);
        when(ticketRepository.findAll(any(Specification.class))).thenReturn(List.of());

        List<Route> result = ticketService.buildRoutes(filters);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ticketRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testBuildRoutes_WithValidFilters_CallsIterativeSearch() {
        TicketFilters filters = new TicketFilters("A", "B", TransportType.BUS, null, null);
        when(ticketRepository.findByDeparture(anyString())).thenReturn(List.of());
        when(ticketGraphService.getTicketGraph()).thenReturn(Map.of(UUID.randomUUID(), List.of()));

        List<Route> result = ticketService.buildRoutes(filters);
        
        assertNotNull(result);
        verify(ticketRepository, times(1)).findByDeparture(anyString());
        verify(ticketGraphService, times(1)).getTicketGraph();
    }
}
