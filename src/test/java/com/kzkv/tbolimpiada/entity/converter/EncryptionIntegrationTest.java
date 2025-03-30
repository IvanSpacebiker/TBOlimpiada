package com.kzkv.tbolimpiada.entity.converter;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import com.kzkv.tbolimpiada.liquibase.LiquibaseRunner;
import com.kzkv.tbolimpiada.repository.BookingRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EncryptionIntegrationTest {

	@Autowired
	private BookingRepository bookingRepository;

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
	void testEncryptionDecryption() {
		Booking booking = Booking.builder()
				.ticket(createSampleTicket())
				.email("test@example.com")
				.phone("123456789")
				.build();

		Booking saved = bookingRepository.save(booking);
		Booking retrieved = bookingRepository.findById(saved.getId()).orElseThrow();

		assertThat(retrieved.getEmail()).isEqualTo("test@example.com");
		assertThat(retrieved.getPhone()).isEqualTo("123456789");
	}

	private Ticket createSampleTicket() {
		Ticket ticket = new Ticket();
		ticket.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
		ticket.setTransportType(TransportType.TRAIN);
		ticket.setPrice(100);
		ticket.setDeparture("CityA");
		ticket.setArrival("CityB");
		ticket.setDepartureDateTime(ZonedDateTime.now());
		ticket.setArrivalDateTime(ZonedDateTime.now().plusHours(2));
		return ticket;
	}
}