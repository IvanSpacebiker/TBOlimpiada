package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.entity.Booking;

import java.util.UUID;

public interface BookingService {

	Booking createBooking(Booking booking);

	void deleteBooking(UUID id);

}
