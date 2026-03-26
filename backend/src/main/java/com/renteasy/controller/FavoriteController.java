package com.renteasy.controller;

import com.renteasy.dto.FavoriteResponse;
import com.renteasy.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TENANT')")
public class FavoriteController {
	private final FavoriteService favoriteService;

	@PostMapping("/{propertyId}")
	public ResponseEntity<Void> add(@PathVariable Long propertyId) {
		favoriteService.add(propertyId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{propertyId}")
	public ResponseEntity<Void> remove(@PathVariable Long propertyId) {
		favoriteService.remove(propertyId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> list() {
		return ResponseEntity.ok(favoriteService.list());
	}
}

