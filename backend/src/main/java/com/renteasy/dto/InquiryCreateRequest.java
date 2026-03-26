package com.renteasy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InquiryCreateRequest(
		@NotNull Long propertyId,
		@NotBlank @Size(max = 2000) String message
) {
}

