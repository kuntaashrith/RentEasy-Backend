package com.renteasy.controller;

import com.renteasy.dto.UserProfileResponse;
import com.renteasy.dto.UserUpdateRequest;
import com.renteasy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<UserProfileResponse> profile() {
		return ResponseEntity.ok(userService.getProfile());
	}

	@PutMapping("/profile")
	public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody UserUpdateRequest req) {
		return ResponseEntity.ok(userService.updateProfile(req));
	}
}

