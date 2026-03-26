package com.renteasy.util;

import com.renteasy.dto.OwnerSummaryResponse;
import com.renteasy.dto.PropertyListItemResponse;
import com.renteasy.dto.PropertyResponse;
import com.renteasy.model.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
	@Mapping(target = "id", source = "property.id")
	@Mapping(target = "title", source = "property.title")
	@Mapping(target = "description", source = "property.description")
	@Mapping(target = "city", source = "property.city")
	@Mapping(target = "address", source = "property.address")
	@Mapping(target = "rent", source = "property.rent")
	@Mapping(target = "bhk", source = "property.bhk")
	@Mapping(target = "propertyType", source = "property.propertyType")
	@Mapping(target = "available", source = "property.available")
	@Mapping(target = "createdAt", source = "property.createdAt")
	@Mapping(target = "owner", expression = "java(owner)")
	@Mapping(target = "imageUrls", expression = "java(imageUrls)")
	PropertyResponse toResponse(Property property, OwnerSummaryResponse owner, List<String> imageUrls);

	default PropertyListItemResponse toListItem(Property property, String thumbnailUrl) {
		return new PropertyListItemResponse(
				property.getId(),
				property.getTitle(),
				property.getCity(),
				property.getRent(),
				property.getBhk(),
				property.getPropertyType(),
				property.getAvailable(),
				thumbnailUrl
		);
	}
}

