package com.kzkv.tbolimpiada.exception;

import java.util.UUID;

public class BookingNotFoundException extends IllegalArgumentException {
	public BookingNotFoundException(UUID id) {
		super("Booking with ID " + id.toString() + " not found.");
	}
}
