package com.kzkv.tbolimpiada.controller;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.exception.BookingNotFoundException;
import com.kzkv.tbolimpiada.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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
		} catch (BookingNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
		Booking created = bookingService.createBooking(booking);
		return ResponseEntity
				.created(URI.create("/bookings/" + created.getId()))
				.body(created);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {
		try {
			bookingService.deleteBooking(id);
			return ResponseEntity.noContent().build();
		} catch (BookingNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
