package com.renteasy.util;

import com.renteasy.dto.InquiryResponse;
import com.renteasy.dto.TenantSummaryResponse;
import com.renteasy.model.Inquiry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InquiryMapper {
	@Mapping(target = "id", source = "inquiry.id")
	@Mapping(target = "propertyId", expression = "java(inquiry.getProperty().getId())")
	@Mapping(target = "tenant", expression = "java(tenant)")
	@Mapping(target = "message", source = "inquiry.message")
	@Mapping(target = "createdAt", source = "inquiry.createdAt")
	InquiryResponse toResponse(Inquiry inquiry, TenantSummaryResponse tenant);
}

