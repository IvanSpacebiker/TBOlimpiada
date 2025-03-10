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
import java.util.Optional;

public class TicketSpecification {

	public static Specification<Ticket> hasDeparture(String departure) {
		return (root, query, cb) -> departure == null || departure.isEmpty() ? null : cb.like(cb.lower(root.get("departure")), "%" + departure.toLowerCase() + "%");
	}

	public static Specification<Ticket> hasArrival(String arrival) {
		return (root, query, cb) -> arrival == null || arrival.isEmpty() ? null : cb.like(cb.lower(root.get("arrival")), "%" + arrival.toLowerCase() + "%");
	}

	public static Specification<Ticket> hasTransportType(TransportType transportType) {
		return (root, query, cb) -> transportType == TransportType.ANY ? null : cb.equal(root.get("transportType"), transportType);
	}

	public static Specification<Ticket> isNotBooked() {
		return (root, query, cb) -> {
			Subquery<Long> subquery = query.subquery(Long.class);
			Root<Booking> bookingRoot = subquery.from(Booking.class);
			subquery.select(cb.literal(1L))
					.where(cb.equal(bookingRoot.get("ticket"), root));

			return cb.not(cb.exists(subquery));
		};
	}

	public static Specification<Ticket> closestToDesiredDateTime(ZonedDateTime desiredDateTime) {
		return (root, query, cb) -> {
			Expression<Long> desiredEpoch = cb.literal(desiredDateTime != null ? desiredDateTime.toEpochSecond() : ZonedDateTime.now().toEpochSecond());
			Expression<Long> timeDifference = cb.abs(cb.diff(root.get("departureEpoch"), desiredEpoch));
			Optional.ofNullable(query).ifPresent(q -> q.orderBy(cb.asc(timeDifference)));
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
