package com.renteasy.dto;

import com.renteasy.model.PropertyType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PropertyResponse(
		Long id,
		String title,
		String description,
		String city,
		String address,
		BigDecimal rent,
		Integer bhk,
		PropertyType propertyType,
		Boolean available,
		OwnerSummaryResponse owner,
		List<String> imageUrls,
		Instant createdAt
) {
}

