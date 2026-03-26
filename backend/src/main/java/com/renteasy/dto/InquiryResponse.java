package com.renteasy.dto;

import java.time.Instant;

public record InquiryResponse(
		Long id,
		Long propertyId,
		TenantSummaryResponse tenant,
		String message,
		Instant createdAt
) {
}

