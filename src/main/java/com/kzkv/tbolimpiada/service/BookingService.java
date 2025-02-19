package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.dto.BookingFilters;
import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.repository.BookingRepository;
import com.kzkv.tbolimpiada.repository.specification.BookingSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {
	private final BookingRepository bookingRepository;

	public Booking createBooking(Booking booking) {
		return bookingRepository.save(booking);
	}

	public Optional<Booking> getBooking(UUID id) {
		return bookingRepository.findById(id);
	}

	public List<Booking> getBookings(BookingFilters filters) {
		return bookingRepository.findAll(BookingSpecification.withFilters(filters));
	}

	public Booking updateBooking(UUID id, Booking booking) {
		if (!bookingRepository.existsById(id)) {
			throw new IllegalArgumentException("Booking with ID " + id + " not found.");
		}
		booking.setId(id);
		return bookingRepository.save(booking);
	}

	public void deleteBooking(UUID id) {
		if (!bookingRepository.existsById(id)) {
			throw new IllegalArgumentException("Booking with ID " + id + " not found.");
		}
		bookingRepository.deleteById(id);
	}

	public boolean isTicketBooked(UUID id) {
		return bookingRepository.exists(BookingSpecification.hasTicketId(id));
	}

}
