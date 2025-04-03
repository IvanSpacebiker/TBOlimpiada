package com.kzkv.tbolimpiada.exception;

public class BookingNotFoundException extends IllegalArgumentException {
	public BookingNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	public BookingNotFoundException(String message) {
		super(message);
	}
}
