package com.ey.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.LaptopCreateRequest;
import com.ey.dto.request.LaptopStockUpdateRequest;
import com.ey.dto.request.LaptopUpdateRequest;
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

	/*
	 * ============================ CREATE (ADMIN) ============================
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<LaptopResponse> createLaptop(@Valid @RequestBody LaptopCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(laptopService.createLaptop(request));
	}

	/*
	 * ============================ READ (PUBLIC) ============================
	 */
	@PreAuthorize("permitAll()")
	@GetMapping
	public ResponseEntity<Page<LaptopResponse>> getLaptops(Pageable pageable) {
		return ResponseEntity.ok(laptopService.getLaptops(pageable));
	}

	/*
	 * ============================ UPDATE DETAILS (ADMIN) ============================
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<LaptopResponse> updateLaptop(@PathVariable Long id,
			@Valid @RequestBody LaptopUpdateRequest request) {
		return ResponseEntity.ok(laptopService.updateLaptop(id, request));
	}

	/*
	 * ============================ UPDATE STOCK (ADMIN, MANAGER) ============================
	 */
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@PatchMapping("/{id}/stock")
	public ResponseEntity<LaptopResponse> updateStock(@PathVariable Long id,
			@Valid @RequestBody LaptopStockUpdateRequest request) {
		return ResponseEntity.ok(laptopService.updateStock(id, request));
	}

	/*
	 * ============================ DELETE (ADMIN) ============================
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLaptop(@PathVariable Long id) {
		laptopService.deleteLaptop(id);
		return ResponseEntity.noContent().build();
	}

	/*
	 * ============================ RESTORE (ADMIN) ============================
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{id}/restore")
	public ResponseEntity<Void> restoreLaptop(@PathVariable Long id) {
		laptopService.restoreLaptop(id);
		return ResponseEntity.ok().build();
	}
}
