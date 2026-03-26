package com.renteasy.controller;

import com.renteasy.dto.PageResponse;
import com.renteasy.dto.PropertyListItemResponse;
import com.renteasy.dto.UserProfileResponse;
import com.renteasy.service.AdminService;
import com.renteasy.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	private final AdminService adminService;
	private final PropertyService propertyService;

	@GetMapping("/users")
	public ResponseEntity<List<UserProfileResponse>> users() {
		return ResponseEntity.ok(adminService.allUsers());
	}

	@GetMapping("/properties")
	public ResponseEntity<PageResponse<PropertyListItemResponse>> properties(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		return ResponseEntity.ok(adminService.allProperties(pageable));
	}

	@DeleteMapping("/properties/{id}")
	public ResponseEntity<Void> removeListing(@PathVariable Long id) {
		propertyService.delete(id);
		return ResponseEntity.noContent().build();
	}
}

