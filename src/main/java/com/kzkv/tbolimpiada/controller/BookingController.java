package com.kzkv.tbolimpiada.controller;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("bookings")
public class BookingController {
	
	private final BookingService bookingService;

	@PostMapping
	public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
		Booking created = bookingService.createBooking(booking);
		return ResponseEntity
				.created(URI.create("/bookings/" + created.getId()))
				.body(created);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {
		try {
			bookingService.deleteBooking(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
