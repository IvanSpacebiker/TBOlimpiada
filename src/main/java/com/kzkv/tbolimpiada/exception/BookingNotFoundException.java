package com.kzkv.tbolimpiada.exception;

import java.util.UUID;

public class BookingNotFoundException extends IllegalArgumentException {
	public BookingNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	public BookingNotFoundException(String message) {}
}
