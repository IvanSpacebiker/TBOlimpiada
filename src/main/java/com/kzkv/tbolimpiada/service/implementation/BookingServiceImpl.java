package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.exception.BookingNotFoundException;
import com.kzkv.tbolimpiada.repository.BookingRepository;
import com.kzkv.tbolimpiada.service.BookingService;
import com.kzkv.tbolimpiada.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingRepository bookingRepository;
	private final EmailService emailService;

	@Transactional(readOnly = true)
	@Cacheable(value = "bookings", key = "#id")
	public Booking getBooking(UUID id) {
		return bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
	}

	@Transactional
	public Booking createBooking(Booking booking) {
		Booking createdBooking = bookingRepository.save(booking);
		emailService.sendEmail(createdBooking.getEmail(), createdBooking);
		return createdBooking;
	}

	@Transactional
	@CacheEvict(value = "bookings", key = "#id")
	public void deleteBooking(UUID id) {
		try {
			bookingRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new BookingNotFoundException(id);
		}
	}
}
