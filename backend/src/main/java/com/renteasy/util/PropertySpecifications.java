package com.renteasy.util;

import com.renteasy.model.Property;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class PropertySpecifications {
	private PropertySpecifications() {
	}

	public static Specification<Property> cityEquals(String city) {
		if (city == null || city.isBlank()) {
			return null;
		}
		return (root, query, cb) -> cb.equal(cb.lower(root.get("city")), city.trim().toLowerCase());
	}

	public static Specification<Property> rentGte(BigDecimal minRent) {
		if (minRent == null) {
			return null;
		}
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("rent"), minRent);
	}

	public static Specification<Property> rentLte(BigDecimal maxRent) {
		if (maxRent == null) {
			return null;
		}
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("rent"), maxRent);
	}

	public static Specification<Property> bhkEquals(Integer bhk) {
		if (bhk == null) {
			return null;
		}
		return (root, query, cb) -> cb.equal(root.get("bhk"), bhk);
	}
}

