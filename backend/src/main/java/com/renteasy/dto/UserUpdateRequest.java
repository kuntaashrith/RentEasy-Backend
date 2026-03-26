package com.renteasy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
		@NotBlank @Size(max = 120) String name,
		@Size(max = 30) String phone
) {
}

