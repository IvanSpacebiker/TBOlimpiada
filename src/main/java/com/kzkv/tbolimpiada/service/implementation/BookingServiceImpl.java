package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.repository.BookingRepository;
import com.kzkv.tbolimpiada.service.BookingService;
import com.kzkv.tbolimpiada.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingRepository bookingRepository;
	private final EmailService emailService;

	public Booking createBooking(Booking booking) {
		Booking bookingEntity = bookingRepository.save(booking);
		emailService.sendEmail(bookingEntity.getEmail(), bookingEntity);
		return bookingEntity;
	}

	public void deleteBooking(UUID id) {
		if (!bookingRepository.existsById(id)) {
			throw new IllegalArgumentException("Booking with ID " + id + " not found.");
		}
		bookingRepository.deleteById(id);
	}

}
