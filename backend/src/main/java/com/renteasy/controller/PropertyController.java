package com.renteasy.controller;

import com.renteasy.dto.PageResponse;
import com.renteasy.dto.PropertyCreateRequest;
import com.renteasy.dto.PropertyListItemResponse;
import com.renteasy.dto.PropertyResponse;
import com.renteasy.dto.PropertyUpdateRequest;
import com.renteasy.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {
	private final PropertyService propertyService;

	@PreAuthorize("hasAnyRole('OWNER','ADMIN')")
	@PostMapping
	public ResponseEntity<PropertyResponse> create(@Valid @RequestBody PropertyCreateRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.create(req));
	}

	@GetMapping
	public ResponseEntity<PageResponse<PropertyListItemResponse>> list(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		return ResponseEntity.ok(propertyService.list(pageable));
	}

	@GetMapping("/search")
	public ResponseEntity<PageResponse<PropertyListItemResponse>> search(
			@RequestParam(required = false) String city,
			@RequestParam(required = false) BigDecimal minRent,
			@RequestParam(required = false) BigDecimal maxRent,
			@RequestParam(required = false) Integer bhk,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		return ResponseEntity.ok(propertyService.search(city, minRent, maxRent, bhk, pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PropertyResponse> get(@PathVariable Long id) {
		return ResponseEntity.ok(propertyService.get(id));
	}

	@PreAuthorize("hasAnyRole('OWNER','ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<PropertyResponse> update(@PathVariable Long id, @Valid @RequestBody PropertyUpdateRequest req) {
		return ResponseEntity.ok(propertyService.update(id, req));
	}

	@PreAuthorize("hasAnyRole('OWNER','ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		propertyService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('OWNER','ADMIN')")
	@GetMapping("/mine")
	public ResponseEntity<List<PropertyListItemResponse>> mine() {
		return ResponseEntity.ok(propertyService.listMine());
	}
}

