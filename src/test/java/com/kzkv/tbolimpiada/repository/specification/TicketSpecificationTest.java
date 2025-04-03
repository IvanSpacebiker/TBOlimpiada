package com.kzkv.tbolimpiada.repository.specification;

import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TicketSpecificationTest {

	@Test
	void testHasDeparture() {
		Specification<Ticket> spec = TicketSpecification.hasDeparture("CityA");

		CriteriaBuilder cb = mock(CriteriaBuilder.class);
		Root<Ticket> root = mock(Root.class);
		Path<String> pathExpression = mock(Path.class);
		Predicate equalPredicate = mock(Predicate.class);

		when(root.get("departure")).thenAnswer(invocation -> pathExpression);
		when(cb.equal(pathExpression, "CityA")).thenReturn(equalPredicate);

		Predicate predicate = spec.toPredicate(root, null, cb);

		assertThat(predicate).isNotNull();
		verify(cb).equal(any(Path.class), eq("CityA"));
	}

	@Test
	void testHasArrival() {
		Specification<Ticket> spec = TicketSpecification.hasArrival("CityB");

		CriteriaBuilder cb = mock(CriteriaBuilder.class);
		Root<Ticket> root = mock(Root.class);
		Path<String> pathExpression = mock(Path.class);
		Predicate equalPredicate = mock(Predicate.class);

		when(root.get("arrival")).thenAnswer(invocation -> pathExpression);
		when(cb.equal(pathExpression, "CityB")).thenReturn(equalPredicate);

		Predicate predicate = spec.toPredicate(root, null, cb);

		assertThat(predicate).isNotNull();
		verify(cb).equal(any(Path.class), eq("CityB"));
	}

	@Test
	void testHasTransportType() {
		Specification<Ticket> spec = TicketSpecification.hasTransportType(TransportType.TRAIN);

		CriteriaBuilder cb = mock(CriteriaBuilder.class);
		Root<Ticket> root = mock(Root.class);
		Path<TransportType> pathExpression = mock(Path.class);
		Predicate equalPredicate = mock(Predicate.class);

		when(root.get("transportType")).thenAnswer(invocation -> pathExpression);
		when(cb.equal(pathExpression, TransportType.TRAIN)).thenReturn(equalPredicate);

		Predicate predicate = spec.toPredicate(root, null, cb);

		assertThat(predicate).isNotNull();
		verify(cb).equal(any(Path.class), eq(TransportType.TRAIN));
	}

	@Test
	void testIsNotBooked() {
		Specification<Ticket> spec = TicketSpecification.isNotBooked();

		CriteriaBuilder cb = mock(CriteriaBuilder.class);
		CriteriaQuery<?> query = mock(CriteriaQuery.class);
		Root<Ticket> root = mock(Root.class);
		Subquery<Long> subquery = mock(Subquery.class);
		Root<Booking> bookingRoot = mock(Root.class);
		Predicate existsPredicate = mock(Predicate.class);

		when(query.subquery(Long.class)).thenReturn(subquery);
		when(subquery.from(Booking.class)).thenReturn(bookingRoot);
		when(subquery.select(cb.literal(1L))).thenReturn(subquery);
		when(cb.exists(subquery)).thenReturn(existsPredicate);
		when(cb.not(existsPredicate)).thenReturn(mock(Predicate.class));

		Predicate predicate = spec.toPredicate(root, query, cb);

		assertThat(predicate).isNotNull();
		verify(cb).not(cb.exists(subquery));
	}

	@Test
	void testWithFilters() {
		TicketFilters filters = new TicketFilters(
				"CityA",
				"CityB",
				TransportType.TRAIN,
				ZonedDateTime.now(),
				ZonedDateTime.now()
		);

		Specification<Ticket> spec = TicketSpecification.withFilters(filters);

		assertThat(spec).isNotNull();
	}
}