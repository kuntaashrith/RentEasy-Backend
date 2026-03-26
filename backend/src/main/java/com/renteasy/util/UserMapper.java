package com.renteasy.util;

import com.renteasy.dto.OwnerSummaryResponse;
import com.renteasy.dto.TenantSummaryResponse;
import com.renteasy.dto.UserProfileResponse;
import com.renteasy.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserProfileResponse toProfile(User user);

	@Mapping(target = "phone", source = "phone")
	OwnerSummaryResponse toOwnerSummary(User user);

	TenantSummaryResponse toTenantSummary(User user);
}

