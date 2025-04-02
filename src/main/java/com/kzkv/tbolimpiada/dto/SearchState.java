package com.kzkv.tbolimpiada.dto;

import com.kzkv.tbolimpiada.entity.Ticket;

import java.time.ZonedDateTime;
import java.util.List;

public record SearchState(

	String current,

	List<Ticket> path,

	ZonedDateTime arrivalTime

) {}