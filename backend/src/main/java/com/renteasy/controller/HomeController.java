package com.renteasy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {
	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> home() {
		return ResponseEntity.ok(Map.of(
				"name", "RentEasy Backend",
				"status", "ok",
				"docs", "/swagger-ui.html",
				"health", "/actuator/health"
		));
	}
}

