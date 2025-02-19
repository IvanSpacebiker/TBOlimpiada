package com.kzkv.tbolimpiada.repository.specification;

import com.kzkv.tbolimpiada.dto.TicketFilters;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Optional;

public class TicketSpecification {

	public static Specification<Ticket> hasDeparture(String departure) {
		return (root, query, cb) -> departure == null ? null : cb.equal(root.get("departure"), departure);
	}

	public static Specification<Ticket> hasArrival(String arrival) {
		return (root, query, cb) -> arrival == null ? null : cb.equal(root.get("arrival"), arrival);
	}

	public static Specification<Ticket> hasTransportType(TransportType transportType) {
		return (root, query, cb) -> transportType == null ? null : cb.equal(root.get("transportType"), transportType);
	}

	public static Specification<Ticket> hasDepartureDateTimeAfterNow() {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("departureDateTime"), LocalDateTime.now());
	}

	public static Specification<Ticket> closestToDesiredDateTime(LocalDateTime desiredDateTime) {
		return (root, query, cb) -> {
			if (desiredDateTime == null) {
				return null;
			}
			Expression<Long> departureEpoch = cb.function("EXTRACT", Long.class, cb.literal("EPOCH"), root.get("departureTime"));
			Expression<Long> desiredEpoch = cb.literal(desiredDateTime.toEpochSecond(java.time.ZoneOffset.UTC));
			Expression<Long> timeDifference = cb.abs(cb.diff(departureEpoch, desiredEpoch));
			Optional.ofNullable(query).ifPresent(q -> q.orderBy(cb.asc(timeDifference)));
			return null;
		};
	}

	public static Specification<Ticket> withFilters(TicketFilters filters) {
		return Specification
				.where(hasDeparture(filters.departure()))
				.and(hasArrival(filters.arrival()))
				.and(hasTransportType(filters.transportType()))
				.and(hasDepartureDateTimeAfterNow())
				.and(closestToDesiredDateTime(filters.departureDateTime()));
	}
}
