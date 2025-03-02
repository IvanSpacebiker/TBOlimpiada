package com.kzkv.tbolimpiada.repository.specification;

import com.kzkv.tbolimpiada.dto.BookingFilters;
import com.kzkv.tbolimpiada.entity.Booking;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class BookingSpecification {

	public static Specification<Booking> hasPhone(String phone) {
		return (root, query, cb) -> phone == null ? null : cb.equal(root.get("phone"), phone);
	}

	public static Specification<Booking> hasEmail(String email) {
		return (root, query, cb) -> email == null ? null : cb.equal(root.get("email"), email);
	}

	public static Specification<Booking> withFilters(BookingFilters filters) {
		return Specification
				.where(hasPhone(filters.phone()))
				.and(hasEmail(filters.email()));
	}
}
