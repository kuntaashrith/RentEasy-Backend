package com.renteasy.controller;

import com.renteasy.dto.AuthLoginRequest;
import com.renteasy.dto.AuthRegisterRequest;
import com.renteasy.dto.AuthResponse;
import com.renteasy.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRegisterRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest req) {
		return ResponseEntity.ok(authService.login(req));
	}
}

