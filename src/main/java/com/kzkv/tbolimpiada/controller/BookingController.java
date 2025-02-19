package com.kzkv.tbolimpiada.controller;

import com.kzkv.tbolimpiada.dto.BookingFilters;
import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("bookings")
public class BookingController {
	
	private final BookingService bookingService;

	@GetMapping("{id}")
	public ResponseEntity<Booking> getBookingById(@PathVariable UUID id) {
		return bookingService.getBooking(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<Booking>> getAllBookings(@ModelAttribute BookingFilters filters) {
		return ResponseEntity.ok(bookingService.getBookings(filters));
	}

	@PostMapping
	public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
		Booking created = bookingService.createBooking(booking);
		return ResponseEntity
				.created(URI.create("/bookings/" + created.getId()))
				.body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Booking> updateBooking(@PathVariable UUID id, @RequestBody Booking booking) {
		try {
			return ResponseEntity.ok(bookingService.updateBooking(id, booking));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {
		try {
			bookingService.deleteBooking(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
