package com.renteasy.dto;

import com.renteasy.model.PropertyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record PropertyUpdateRequest(
		@Size(max = 160) String title,
		String description,
		@Size(max = 80) String city,
		@Size(max = 255) String address,
		@DecimalMin(value = "0.0", inclusive = false) BigDecimal rent,
		Integer bhk,
		PropertyType propertyType,
		Boolean available,
		List<@Size(max = 1000) String> imageUrls
) {
}

