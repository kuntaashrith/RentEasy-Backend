package com.renteasy.dto;

public record AuthResponse(
		String accessToken,
		String tokenType,
		long expiresInSeconds,
		UserProfileResponse user
) {
}

