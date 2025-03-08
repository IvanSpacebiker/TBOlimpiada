package com.kzkv.tbolimpiada.controller;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("bookings")
public class BookingController {

	private final BookingService bookingService;

	@GetMapping("{id}")
	public ResponseEntity<Booking> getBooking(@PathVariable UUID id) {
		try {
			Booking booking = bookingService.getBooking(id);
			return ResponseEntity.ok(booking);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<Booking> createBooking(@RequestBody Booking booking, HttpServletRequest request) {
		Booking created = bookingService.createBooking(booking, request);
		return ResponseEntity
				.created(URI.create("/bookings/" + created.getId()))
				.body(created);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {
		try {
			bookingService.deleteBooking(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
