package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.dto.BookingFilters;
import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.repository.BookingRepository;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import com.kzkv.tbolimpiada.repository.specification.BookingSpecification;
import com.kzkv.tbolimpiada.service.BookingService;
import com.kzkv.tbolimpiada.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingRepository bookingRepository;
	private final TicketRepository ticketRepository;
	private final EmailService emailService;

	public Booking createBooking(Booking booking) {
		Booking bookingEntity = bookingRepository.save(booking);
		emailService.sendEmail(bookingEntity.getEmail(), bookingEntity);
		return bookingEntity;
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
		return bookingRepository.exists(Example.of(
				Booking.builder()
						.ticket(ticketRepository.findById(id).orElseThrow())
						.build()));
	}

}
