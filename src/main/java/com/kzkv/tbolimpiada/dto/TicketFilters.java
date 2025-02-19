package com.kzkv.tbolimpiada.dto;

import com.kzkv.tbolimpiada.entity.TransportType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record TicketFilters(
		String departure,
		String arrival,
		TransportType transportType,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		LocalDateTime departureDateTime
) {
}
