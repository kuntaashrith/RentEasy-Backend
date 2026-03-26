package com.renteasy.service;

import com.renteasy.dto.OwnerSummaryResponse;
import com.renteasy.dto.PageResponse;
import com.renteasy.dto.PropertyCreateRequest;
import com.renteasy.dto.PropertyListItemResponse;
import com.renteasy.dto.PropertyResponse;
import com.renteasy.dto.PropertyUpdateRequest;
import com.renteasy.exception.ForbiddenException;
import com.renteasy.exception.ResourceNotFoundException;
import com.renteasy.model.Property;
import com.renteasy.model.PropertyImage;
import com.renteasy.model.Role;
import com.renteasy.model.User;
import com.renteasy.repository.PropertyImageRepository;
import com.renteasy.repository.PropertyRepository;
import com.renteasy.repository.UserRepository;
import com.renteasy.security.AuthUser;
import com.renteasy.security.SecurityUtil;
import com.renteasy.util.PageUtil;
import com.renteasy.util.PropertyMapper;
import com.renteasy.util.PropertySpecifications;
import com.renteasy.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {
	private final PropertyRepository propertyRepository;
	private final PropertyImageRepository propertyImageRepository;
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PropertyMapper propertyMapper;

	@Transactional
	public PropertyResponse create(PropertyCreateRequest req) {
		AuthUser au = SecurityUtil.requireAuthUser();
		if (au.getRole() != Role.OWNER && au.getRole() != Role.ADMIN) {
			throw new ForbiddenException("Only owners can add properties");
		}
		User owner = userRepository.findById(au.getId()).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

		Property property = Property.builder()
				.title(req.title())
				.description(req.description())
				.city(req.city())
				.address(req.address())
				.rent(req.rent())
				.bhk(req.bhk())
				.propertyType(req.propertyType())
				.available(req.available() != null ? req.available() : Boolean.TRUE)
				.owner(owner)
				.build();

		propertyRepository.save(property);

		List<String> imageUrls = saveImages(property, req.imageUrls());
		return toPropertyResponse(property, imageUrls);
	}

	@Transactional(readOnly = true)
	public PageResponse<PropertyListItemResponse> list(Pageable pageable) {
		Page<Property> page = propertyRepository.findAll(pageable);
		Page<PropertyListItemResponse> mapped = page.map(p -> propertyMapper.toListItem(p, firstImageUrl(p.getId())));
		return PageUtil.toPageResponse(mapped);
	}

	@Transactional(readOnly = true)
	public PageResponse<PropertyListItemResponse> search(String city, BigDecimal minRent, BigDecimal maxRent, Integer bhk, Pageable pageable) {
		Specification<Property> spec = Specification.where(PropertySpecifications.cityEquals(city))
				.and(PropertySpecifications.rentGte(minRent))
				.and(PropertySpecifications.rentLte(maxRent))
				.and(PropertySpecifications.bhkEquals(bhk));
		Page<Property> page = propertyRepository.findAll(spec, pageable);
		Page<PropertyListItemResponse> mapped = page.map(p -> propertyMapper.toListItem(p, firstImageUrl(p.getId())));
		return PageUtil.toPageResponse(mapped);
	}

	@Transactional(readOnly = true)
	public PropertyResponse get(Long id) {
		Property p = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
		List<String> imageUrls = propertyImageRepository.findByPropertyId(id).stream().map(PropertyImage::getImageUrl).toList();
		return toPropertyResponse(p, imageUrls);
	}

	@Transactional
	public PropertyResponse update(Long id, PropertyUpdateRequest req) {
		AuthUser au = SecurityUtil.requireAuthUser();
		Property p = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
		requireOwnerOrAdmin(au, p);

		if (req.title() != null) p.setTitle(req.title());
		if (req.description() != null) p.setDescription(req.description());
		if (req.city() != null) p.setCity(req.city());
		if (req.address() != null) p.setAddress(req.address());
		if (req.rent() != null) p.setRent(req.rent());
		if (req.bhk() != null) p.setBhk(req.bhk());
		if (req.propertyType() != null) p.setPropertyType(req.propertyType());
		if (req.available() != null) p.setAvailable(req.available());

		List<String> imageUrls;
		if (req.imageUrls() != null) {
			propertyImageRepository.deleteByPropertyId(p.getId());
			imageUrls = saveImages(p, req.imageUrls());
		} else {
			imageUrls = propertyImageRepository.findByPropertyId(p.getId()).stream().map(PropertyImage::getImageUrl).toList();
		}
		return toPropertyResponse(p, imageUrls);
	}

	@Transactional
	public void delete(Long id) {
		AuthUser au = SecurityUtil.requireAuthUser();
		Property p = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
		requireOwnerOrAdmin(au, p);
		propertyImageRepository.deleteByPropertyId(p.getId());
		propertyRepository.delete(p);
	}

	@Transactional(readOnly = true)
	public List<PropertyListItemResponse> listMine() {
		AuthUser au = SecurityUtil.requireAuthUser();
		List<Property> props = propertyRepository.findByOwnerIdOrderByCreatedAtDesc(au.getId());
		List<PropertyListItemResponse> out = new ArrayList<>(props.size());
		for (Property p : props) {
			out.add(propertyMapper.toListItem(p, firstImageUrl(p.getId())));
		}
		return out;
	}

	private void requireOwnerOrAdmin(AuthUser au, Property p) {
		if (au.getRole() == Role.ADMIN) return;
		if (au.getRole() != Role.OWNER) throw new ForbiddenException("Only owner can modify this property");
		if (!p.getOwner().getId().equals(au.getId())) throw new ForbiddenException("Not your property");
	}

	private PropertyResponse toPropertyResponse(Property p, List<String> imageUrls) {
		OwnerSummaryResponse owner = userMapper.toOwnerSummary(p.getOwner());
		return propertyMapper.toResponse(p, owner, imageUrls);
	}

	private List<String> saveImages(Property property, List<String> imageUrls) {
		if (imageUrls == null || imageUrls.isEmpty()) {
			return List.of();
		}
		List<PropertyImage> images = imageUrls.stream()
				.filter(u -> u != null && !u.isBlank())
				.map(u -> PropertyImage.builder().property(property).imageUrl(u.trim()).build())
				.toList();
		propertyImageRepository.saveAll(images);
		return images.stream().map(PropertyImage::getImageUrl).toList();
	}

	private String firstImageUrl(Long propertyId) {
		return propertyImageRepository.findByPropertyId(propertyId).stream()
				.findFirst()
				.map(PropertyImage::getImageUrl)
				.orElse(null);
	}
}

