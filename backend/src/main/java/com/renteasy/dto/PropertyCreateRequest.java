package com.renteasy.dto;

import com.renteasy.model.PropertyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record PropertyCreateRequest(
		@NotBlank @Size(max = 160) String title,
		String description,
		@NotBlank @Size(max = 80) String city,
		@NotBlank @Size(max = 255) String address,
		@NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal rent,
		@NotNull Integer bhk,
		@NotNull PropertyType propertyType,
		Boolean available,
		List<@NotBlank @Size(max = 1000) String> imageUrls
) {
}

