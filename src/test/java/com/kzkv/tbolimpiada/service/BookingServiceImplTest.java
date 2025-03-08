package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import com.kzkv.tbolimpiada.repository.BookingRepository;
import com.kzkv.tbolimpiada.service.implementation.BookingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private BookingServiceImpl bookingService;

	@Test
	void testCreateBooking() {
		Booking booking = createSampleBooking();

		when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

		Booking result = bookingService.createBooking(booking, mock(HttpServletRequest.class));

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(booking.getId());
		verify(bookingRepository).save(any(Booking.class));
		verify(emailService).sendEmail(anyString(), eq(booking), any(HttpServletRequest.class));
	}

	@Test
	void testGetBooking_Existing() {
		UUID id = UUID.randomUUID();
		Booking booking = createSampleBooking();

		when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

		Booking result = bookingService.getBooking(id);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(booking.getId());
	}

	@Test
	void testGetBooking_NonExisting() {
		UUID id = UUID.randomUUID();

		when(bookingRepository.findById(id)).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> bookingService.getBooking(id));

		assertThat(thrown).isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void testDeleteBooking_Existing() {
		UUID id = UUID.randomUUID();

		when(bookingRepository.existsById(id)).thenReturn(true);

		assertDoesNotThrow(() -> bookingService.deleteBooking(id));
		verify(bookingRepository).deleteById(id);
	}

	@Test
	void testDeleteBooking_NonExisting() {
		UUID id = UUID.randomUUID();

		when(bookingRepository.existsById(id)).thenReturn(false);

		Throwable thrown = catchThrowable(() -> bookingService.deleteBooking(id));

		assertThat(thrown)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("not found");
		verify(bookingRepository, never()).deleteById(any(UUID.class));
	}

	private Booking createSampleBooking() {
		return Booking.builder()
				.id(UUID.randomUUID())
				.ticket(createSampleTicket())
				.phone("123456789")
				.email("test@example.com")
				.build();
	}

	private Ticket createSampleTicket() {
		Ticket ticket = new Ticket();
		ticket.setId(UUID.randomUUID());
		ticket.setTransportType(TransportType.TRAIN);
		ticket.setPrice(100);
		ticket.setDeparture("CityA");
		ticket.setArrival("CityB");
		ticket.setDepartureDateTime(ZonedDateTime.now());
		ticket.setArrivalDateTime(ZonedDateTime.now().plusHours(2));
		return ticket;
	}
}