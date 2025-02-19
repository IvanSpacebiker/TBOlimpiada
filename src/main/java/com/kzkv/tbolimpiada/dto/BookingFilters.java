package com.kzkv.tbolimpiada.dto;

import java.util.UUID;

public record BookingFilters(
		UUID routeId,
		String phone,
		String email
) {
}
