package com.renteasy.dto;

import com.renteasy.model.Role;

import java.time.Instant;

public record UserProfileResponse(
		Long id,
		String name,
		String email,
		String phone,
		Role role,
		Instant createdAt
) {
}

