package com.kzkv.tbolimpiada.dto;

import com.kzkv.tbolimpiada.entity.Ticket;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public record SearchState(

	String current,

	List<Ticket> path,

	ZonedDateTime arrivalTime,

	Set<String> visited

) {}