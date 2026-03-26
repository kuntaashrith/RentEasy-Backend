package com.renteasy.dto;

import com.renteasy.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthRegisterRequest(
		@NotBlank @Size(max = 120) String name,
		@Email @NotBlank @Size(max = 200) String email,
		@NotBlank @Size(min = 8, max = 100) String password,
		@Size(max = 30) String phone,
		@NotNull Role role
) {
}

