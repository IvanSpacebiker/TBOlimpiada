package com.kzkv.tbolimpiada.dto;

import com.kzkv.tbolimpiada.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
public class SearchState {
    String current;
    List<Ticket> path;
    ZonedDateTime arrivalTime;
    Set<String> visited;

}