package com.ey.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.LaptopCreateRequest;
import com.ey.dto.response.LaptopResponse;
import com.ey.service.LaptopService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/laptops")
public class LaptopController {

	private final LaptopService laptopService;

	public LaptopController(LaptopService laptopService) {
		this.laptopService = laptopService;
	}

	// ADMIN ONLY
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<LaptopResponse> createLaptop(@Valid @RequestBody LaptopCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(laptopService.createLaptop(request));
	}

	// PUBLIC
	@GetMapping
	public ResponseEntity<Page<LaptopResponse>> getLaptops(Pageable pageable) {
		return ResponseEntity.ok(laptopService.getLaptops(pageable));
	}
}
