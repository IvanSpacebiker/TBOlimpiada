package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.dto.BookingFilters;
import com.kzkv.tbolimpiada.entity.Booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingService {

	Booking createBooking(Booking booking);

	Optional<Booking> getBooking(UUID id);

	List<Booking> getBookings(BookingFilters filters);

	Booking updateBooking(UUID id, Booking booking);

	void deleteBooking(UUID id);

	boolean isTicketBooked(UUID id);

}
