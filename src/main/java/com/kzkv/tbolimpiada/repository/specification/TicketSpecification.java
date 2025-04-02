package com.kzkv.tbolimpiada.repository.specification;

import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class TicketSpecification {

	private TicketSpecification() {
		throw new IllegalStateException("Specification class");
	}

	public static Specification<Ticket> hasDeparture(String departure) {
		return (root, query, cb) -> departure == null || departure.isEmpty() ? null : cb.equal(root.get("departure"), departure);
	}

	public static Specification<Ticket> hasArrival(String arrival) {
		return (root, query, cb) -> arrival == null || arrival.isEmpty() ? null : cb.equal(root.get("arrival"), arrival);
	}

	public static Specification<Ticket> hasTransportType(TransportType transportType) {
		return (root, query, cb) -> transportType == TransportType.ANY ? null : cb.equal(root.get("transportType"), transportType);
	}

	public static Specification<Ticket> isNotBooked() {
		return (root, query, cb) -> {
			Subquery<Long> subquery = query.subquery(Long.class);
			Root<Booking> bookingRoot = subquery.from(Booking.class);
			subquery.select(cb.literal(1L)).where(cb.equal(bookingRoot.get("ticket"), root));

			return cb.not(cb.exists(subquery));
		};
	}

	public static Specification<Ticket> closestToDesiredDateTime(ZonedDateTime desiredDateTime) {
		return (root, query, cb) -> {
			Expression<Long> desiredEpoch = cb.literal(desiredDateTime != null ? desiredDateTime.toEpochSecond() : ZonedDateTime.now().toEpochSecond());
			Expression<Long> timeDifference = cb.abs(cb.diff(root.get("departureEpoch"), desiredEpoch));
			query.orderBy(cb.asc(timeDifference));
			return null;
		};
	}

	public static Specification<Ticket> hasDepartureDateTimeAfterBorder(ZonedDateTime borderDateTime) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("departureDateTime"), borderDateTime != null ? borderDateTime : ZonedDateTime.now());
	}

	public static Specification<Ticket> withFilters(TicketFilters filters) {
		return Specification
				.where(hasDeparture(filters.departure()))
				.and(hasArrival(filters.arrival()))
				.and(hasTransportType(filters.transportType()))
				.and(closestToDesiredDateTime(filters.desiredDateTime()))
				.and(hasDepartureDateTimeAfterBorder(filters.borderDateTime()))
				.and(isNotBooked());
	}
}
