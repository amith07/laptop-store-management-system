package com.ey.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.BrandCreateRequest;
import com.ey.dto.request.BrandUpdateRequest;
import com.ey.dto.response.BrandResponse;
import com.ey.service.BrandService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

	private final BrandService brandService;

	public BrandController(BrandService brandService) {
		this.brandService = brandService;
	}

	// ============================
	// CREATE BRAND (ADMIN)
	// ============================
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<BrandResponse> createBrand(@Valid @RequestBody BrandCreateRequest request) {
		BrandResponse response = brandService.createBrand(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// ============================
	// LIST BRANDS (PUBLIC)
	// ============================
	@GetMapping
	public ResponseEntity<Page<BrandResponse>> getBrands(@PageableDefault(size = 10) Pageable pageable) {
		Page<BrandResponse> response = brandService.getBrands(pageable);
		return ResponseEntity.ok(response);
	}

	// ============================
	// UPDATE BRAND (ADMIN)
	// ============================
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<BrandResponse> updateBrand(@PathVariable Long id,
			@Valid @RequestBody BrandUpdateRequest request) {
		BrandResponse response = brandService.updateBrand(id, request);
		return ResponseEntity.ok(response);
	}

	// ============================
	// DELETE BRAND (ADMIN, SOFT)
	// ============================
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
		brandService.deleteBrand(id, "ADMIN");
		return ResponseEntity.noContent().build();
	}

	// ============================
	// RESTORE BRAND (ADMIN)
	// ============================
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{id}/restore")
	public ResponseEntity<Void> restoreBrand(@PathVariable Long id) {
		brandService.restoreBrand(id);
		return ResponseEntity.ok().build();
	}
}
