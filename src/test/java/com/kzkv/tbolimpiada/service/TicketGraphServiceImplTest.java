package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TicketGraph;
import com.kzkv.tbolimpiada.repository.TicketGraphRepository;
import com.kzkv.tbolimpiada.service.implementation.TicketGraphServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketGraphServiceImplTest {

    @Mock
    private TicketGraphRepository ticketGraphRepository;

    @InjectMocks
    private TicketGraphServiceImpl ticketGraphService;

    @Test
    void testGetTicketGraph_ReturnsCorrectMapping() {
        Ticket ticket1 = mock(Ticket.class);
        Ticket ticket2 = mock(Ticket.class);
        TicketGraph graph = mock(TicketGraph.class);
        when(ticket1.getId()).thenReturn(UUID.randomUUID());
        when(graph.getTicket()).thenReturn(ticket1);
        when(graph.getNextTicket()).thenReturn(ticket2);

        when(ticketGraphRepository.findAll()).thenReturn(List.of(graph));

        Map<UUID, List<Ticket>> result = ticketGraphService.getTicketGraph();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ticketGraphRepository, times(1)).findAll();
    }

    @Test
    void testBuildTicketGraph_ClearsAndSavesNewGraph() {
        Ticket ticket1 = mock(Ticket.class);
        Ticket ticket2 = mock(Ticket.class);
        when(ticket1.getDeparture()).thenReturn("CityA");
        when(ticket1.getArrival()).thenReturn("CityB");
        when(ticket2.getDeparture()).thenReturn("CityB");
        when(ticket2.getArrival()).thenReturn("CityC");
        when(ticket2.getDepartureDateTime()).thenReturn(mock(java.time.ZonedDateTime.class));
        when(ticket1.getArrivalDateTime()).thenReturn(mock(java.time.ZonedDateTime.class));

        ticketGraphService.buildTicketGraph(List.of(ticket1, ticket2));

        verify(ticketGraphRepository, times(1)).deleteAll();
        verify(ticketGraphRepository, times(1)).saveAll(any());
    }
}