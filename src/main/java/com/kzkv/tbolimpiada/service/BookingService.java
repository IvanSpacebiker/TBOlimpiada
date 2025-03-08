package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.entity.Booking;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface BookingService {

	Booking getBooking(UUID id);

	Booking createBooking(Booking booking, HttpServletRequest request);

	void deleteBooking(UUID id);

}
