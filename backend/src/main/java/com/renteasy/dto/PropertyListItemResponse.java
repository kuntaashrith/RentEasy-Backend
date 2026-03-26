package com.renteasy.dto;

import com.renteasy.model.PropertyType;

import java.math.BigDecimal;

public record PropertyListItemResponse(
		Long id,
		String title,
		String city,
		BigDecimal rent,
		Integer bhk,
		PropertyType propertyType,
		Boolean available,
		String thumbnailUrl
) {
}

