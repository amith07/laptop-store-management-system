package com.ey.service;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ey.dto.request.LaptopCreateRequest;
import com.ey.dto.request.LaptopSearchRequest;
import com.ey.dto.request.LaptopStockUpdateRequest;
import com.ey.dto.request.LaptopUpdateRequest;
import com.ey.dto.response.LaptopResponse;
import com.ey.enums.ApiErrorCode;
import com.ey.exception.ApiException;
import com.ey.model.Brand;
import com.ey.model.Laptop;
import com.ey.repository.BrandRepository;
import com.ey.repository.LaptopRepository;
import com.ey.repository.spec.LaptopSpecification;

@Service
@Transactional
public class LaptopService {

	private final LaptopRepository laptopRepository;
	private final BrandRepository brandRepository;

	public LaptopService(LaptopRepository laptopRepository, BrandRepository brandRepository) {
		this.laptopRepository = laptopRepository;
		this.brandRepository = brandRepository;
	}

	/*
	 * ============================ CREATE ============================
	 */
	public LaptopResponse createLaptop(LaptopCreateRequest request) {

		// Validate brand FIRST
		Brand brand = brandRepository.findByIdAndDeletedAtIsNull(request.getBrandId()).orElseThrow(
				() -> new ApiException(ApiErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND, "Brand not found"));

		// Validate serial
		if (laptopRepository.existsBySerialNumber(request.getSerialNumber())) {
			throw new ApiException(ApiErrorCode.SERIAL_EXISTS, HttpStatus.CONFLICT, "Serial number already exists");
		}

		Laptop laptop = new Laptop();
		laptop.setBrand(brand);
		laptop.setModelName(request.getModelName());
		laptop.setSerialNumber(request.getSerialNumber());
		laptop.setCpu(request.getCpu());
		laptop.setRamGb(request.getRamGb());
		laptop.setStorageGb(request.getStorageGb());
		laptop.setScreenSizeInches(request.getScreenSizeInches());
		laptop.setPrice(request.getPrice());
		laptop.setDiscountPercentage(request.getDiscountPercentage());
		laptop.setPromotionalPrice(request.getPromotionalPrice());
		laptop.setStock(request.getStock());

		return toResponse(laptopRepository.save(laptop));
	}

	/*
	 * ============================ READ (PUBLIC) ============================
	 */
	@Transactional(readOnly = true)
	public Page<LaptopResponse> getLaptops(Pageable pageable) {
		return laptopRepository.findAll(LaptopSpecification.build(null, null, null, null, null), pageable)
				.map(this::toResponse);
	}

	/*
	 * ============================ SEARCH / FILTER ============================
	 */
	@Transactional(readOnly = true)
	public Page<LaptopResponse> searchLaptops(LaptopSearchRequest request, Pageable pageable) {
		return laptopRepository.findAll(LaptopSpecification.build(request.getBrandId(), request.getCpu(),
				request.getMinPrice(), request.getMaxPrice(), request.getStatus()), pageable).map(this::toResponse);
	}

	/*
	 * ============================ UPDATE DETAILS (ADMIN) ============================
	 */
	public LaptopResponse updateLaptop(Long id, LaptopUpdateRequest request) {

		Laptop laptop = laptopRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
				() -> new ApiException(ApiErrorCode.LAPTOP_NOT_FOUND, HttpStatus.NOT_FOUND, "Laptop not found"));

		if (request.getModelName() != null)
			laptop.setModelName(request.getModelName());
		if (request.getCpu() != null)
			laptop.setCpu(request.getCpu());
		if (request.getRamGb() != null)
			laptop.setRamGb(request.getRamGb());
		if (request.getStorageGb() != null)
			laptop.setStorageGb(request.getStorageGb());
		if (request.getScreenSizeInches() != null)
			laptop.setScreenSizeInches(request.getScreenSizeInches());
		if (request.getPrice() != null)
			laptop.setPrice(request.getPrice());
		if (request.getDiscountPercentage() != null)
			laptop.setDiscountPercentage(request.getDiscountPercentage());
		if (request.getPromotionalPrice() != null)
			laptop.setPromotionalPrice(request.getPromotionalPrice());

		// 🔴 Force recalculation for response correctness
		laptop.computeEffectivePrice();
		laptop.updateStatus();

		return toResponse(laptop);
	}

	/*
	 * ============================ UPDATE STOCK (ADMIN, MANAGER) ============================
	 */
	public LaptopResponse updateStock(Long id, LaptopStockUpdateRequest request) {

		Laptop laptop = laptopRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
				() -> new ApiException(ApiErrorCode.LAPTOP_NOT_FOUND, HttpStatus.NOT_FOUND, "Laptop not found"));

		laptop.setStock(request.getStock());

		// 🔴 Force status recalculation
		laptop.updateStatus();

		return toResponse(laptop);
	}

	/*
	 * ============================ SOFT DELETE (ADMIN) ============================
	 */
	public void deleteLaptop(Long id) {

		Laptop laptop = laptopRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
				() -> new ApiException(ApiErrorCode.LAPTOP_NOT_FOUND, HttpStatus.NOT_FOUND, "Laptop not found"));

		laptop.setDeletedAt(Instant.now());
	}

	/*
	 * ============================ RESTORE (ADMIN) ============================
	 */
	public void restoreLaptop(Long id) {

		Laptop laptop = laptopRepository.findById(id).orElseThrow(
				() -> new ApiException(ApiErrorCode.LAPTOP_NOT_FOUND, HttpStatus.NOT_FOUND, "Laptop not found"));

		if (laptop.getDeletedAt() == null) {
			throw new ApiException(ApiErrorCode.ALREADY_ACTIVE, HttpStatus.CONFLICT, "Laptop is already active");
		}

		laptop.setDeletedAt(null);
	}

	/*
	 * ============================ MAPPER ============================
	 */
	private LaptopResponse toResponse(Laptop laptop) {
		return new LaptopResponse(laptop.getId(), laptop.getBrand().getName(), laptop.getModelName(),
				laptop.getEffectivePrice(), laptop.getStock(), laptop.getStatus());
	}
}
