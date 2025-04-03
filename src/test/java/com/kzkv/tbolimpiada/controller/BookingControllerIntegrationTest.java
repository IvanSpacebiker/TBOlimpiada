package com.kzkv.tbolimpiada.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.liquibase.LiquibaseRunner;
import com.kzkv.tbolimpiada.repository.BookingRepository;
import com.kzkv.tbolimpiada.repository.TicketRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "generator.enabled=false")
class BookingControllerIntegrationTest {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Container
	private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
			.withDatabaseName("testdb")
			.withUsername("testuser")
			.withPassword("testpassword");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@BeforeAll
	static void setup() throws Exception {
		postgres.start();
		LiquibaseRunner.runMigrations(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
	}

	@Test
	void testCreateAndGetBooking() throws Exception {
		Booking request = Booking.builder()
				.ticket(ticketRepository.findById(UUID.fromString("123e4567-e89b-12d3-a456-426614174001")).get())
				.email("test@example.com")
				.phone("123456789")
				.build();

		MvcResult createResult = mockMvc.perform(post("/bookings")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andReturn();

		String location = createResult.getResponse().getHeader("Location");
		String bookingId = location.substring(location.lastIndexOf("/") + 1);

		mockMvc.perform(get("/bookings/{id}", bookingId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("test@example.com"))
				.andExpect(jsonPath("$.phone").value("123456789"));
	}

	@Test
	void testDeleteBooking() throws Exception {
		Ticket ticket = ticketRepository.findById(UUID.fromString("123e4567-e89b-12d3-a456-426614174001")).get();
		Booking booking = bookingRepository.findOne(
				Example.of(
						Booking.builder()
								.ticket(ticket)
								.email("test@example.com")
								.phone("123456789")
								.build()
				)).get();

		mockMvc.perform(delete("/bookings/{id}", booking.getId()))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/bookings/{id}", booking.getId()))
				.andExpect(status().isNotFound());
	}

}