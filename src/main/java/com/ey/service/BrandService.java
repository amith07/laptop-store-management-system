package com.ey.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ey.dto.request.BrandCreateRequest;
import com.ey.dto.request.BrandUpdateRequest;
import com.ey.dto.response.BrandResponse;
import com.ey.enums.ApiErrorCode;
import com.ey.exception.ApiException;
import com.ey.model.Brand;
import com.ey.repository.BrandRepository;

@Service
@Transactional
public class BrandService {

	private final BrandRepository brandRepository;

	public BrandService(BrandRepository brandRepository) {
		this.brandRepository = brandRepository;
	}

	public BrandResponse createBrand(BrandCreateRequest request) {

		if (brandRepository.existsByNameIgnoreCase(request.getName())) {
			throw new ApiException(ApiErrorCode.BRAND_EXISTS, HttpStatus.CONFLICT, "Brand already exists");
		}

		Brand brand = new Brand();
		brand.setName(request.getName());
		brand.setCountry(request.getCountry());
		brand.setActive(request.isActive());

		Brand saved = brandRepository.save(brand);

		return toResponse(saved);
	}

	@Transactional(readOnly = true)
	public Page<BrandResponse> getBrands(Pageable pageable) {
		return brandRepository.findAllByDeletedAtIsNull(pageable).map(this::toResponse);
	}

	public BrandResponse updateBrand(Long id, BrandUpdateRequest request) {

		Brand brand = brandRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
				() -> new ApiException(ApiErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND, "Brand not found"));

		if (request.getCountry() != null) {
			brand.setCountry(request.getCountry());
		}

		if (request.getActive() != null) {
			brand.setActive(request.getActive());
		}

		return toResponse(brand);
	}

	public void deleteBrand(Long id, String deletedBy) {

		Brand brand = brandRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
				() -> new ApiException(ApiErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND, "Brand not found"));

		brand.setDeletedAt(java.time.Instant.now());
		brand.setDeletedBy(deletedBy);
		brand.setActive(false);
	}

	public void restoreBrand(Long id) {

		Brand brand = brandRepository.findById(id).orElseThrow(
				() -> new ApiException(ApiErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND, "Brand not found"));

		if (brand.getDeletedAt() == null) {
			throw new ApiException(ApiErrorCode.ALREADY_ACTIVE, HttpStatus.CONFLICT, "Brand is already active");
		}

		brand.setDeletedAt(null);
		brand.setDeletedBy(null);
		brand.setActive(true);
	}

	private BrandResponse toResponse(Brand brand) {
		return new BrandResponse(brand.getId(), brand.getName(), brand.getCountry(), brand.isActive());
	}
}
