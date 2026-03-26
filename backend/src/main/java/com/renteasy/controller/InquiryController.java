package com.renteasy.controller;

import com.renteasy.dto.InquiryCreateRequest;
import com.renteasy.dto.InquiryResponse;
import com.renteasy.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {
	private final InquiryService inquiryService;

	@PostMapping
	public ResponseEntity<InquiryResponse> create(@Valid @RequestBody InquiryCreateRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(inquiryService.create(req));
	}

	@GetMapping("/property/{propertyId}")
	public ResponseEntity<List<InquiryResponse>> listByProperty(@PathVariable Long propertyId) {
		return ResponseEntity.ok(inquiryService.listByProperty(propertyId));
	}
}

