package com.kzkv.tbolimpiada.dto;

import com.kzkv.tbolimpiada.entity.TransportType;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

public record TicketFilters(
		String departure,
		String arrival,
		@NonNull
		TransportType transportType,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		ZonedDateTime desiredDateTime,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		ZonedDateTime borderDateTime
) {
}
