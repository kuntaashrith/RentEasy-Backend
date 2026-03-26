package com.renteasy.service;

import com.renteasy.dto.InquiryCreateRequest;
import com.renteasy.dto.InquiryResponse;
import com.renteasy.dto.TenantSummaryResponse;
import com.renteasy.exception.ForbiddenException;
import com.renteasy.exception.ResourceNotFoundException;
import com.renteasy.model.Inquiry;
import com.renteasy.model.Property;
import com.renteasy.model.Role;
import com.renteasy.model.User;
import com.renteasy.repository.InquiryRepository;
import com.renteasy.repository.PropertyRepository;
import com.renteasy.repository.UserRepository;
import com.renteasy.security.AuthUser;
import com.renteasy.security.SecurityUtil;
import com.renteasy.util.InquiryMapper;
import com.renteasy.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {
	private final InquiryRepository inquiryRepository;
	private final PropertyRepository propertyRepository;
	private final UserRepository userRepository;
	private final InquiryMapper inquiryMapper;
	private final UserMapper userMapper;

	@Transactional
	public InquiryResponse create(InquiryCreateRequest req) {
		AuthUser au = SecurityUtil.requireAuthUser();
		if (au.getRole() != Role.TENANT) {
			throw new ForbiddenException("Only tenants can send inquiries");
		}
		Property property = propertyRepository.findById(req.propertyId()).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
		User tenant = userRepository.findById(au.getId()).orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

		Inquiry inquiry = Inquiry.builder()
				.property(property)
				.tenant(tenant)
				.message(req.message())
				.build();
		inquiryRepository.save(inquiry);

		TenantSummaryResponse tenantSummary = userMapper.toTenantSummary(tenant);
		return inquiryMapper.toResponse(inquiry, tenantSummary);
	}

	@Transactional(readOnly = true)
	public List<InquiryResponse> listByProperty(Long propertyId) {
		AuthUser au = SecurityUtil.requireAuthUser();
		Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
		if (au.getRole() != Role.ADMIN) {
			if (au.getRole() != Role.OWNER || !property.getOwner().getId().equals(au.getId())) {
				throw new ForbiddenException("Not allowed to view inquiries for this property");
			}
		}

		List<Inquiry> inquiries = inquiryRepository.findByPropertyIdOrderByCreatedAtDesc(propertyId);
		List<InquiryResponse> out = new ArrayList<>(inquiries.size());
		for (Inquiry i : inquiries) {
			TenantSummaryResponse tenant = userMapper.toTenantSummary(i.getTenant());
			out.add(inquiryMapper.toResponse(i, tenant));
		}
		return out;
	}
}

