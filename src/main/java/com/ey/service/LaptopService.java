package com.ey.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ey.dto.request.LaptopCreateRequest;
import com.ey.dto.response.LaptopResponse;
import com.ey.enums.ApiErrorCode;
import com.ey.exception.ApiException;
import com.ey.model.Brand;
import com.ey.model.Laptop;
import com.ey.repository.BrandRepository;
import com.ey.repository.LaptopRepository;

@Service
@Transactional
public class LaptopService {

	private final LaptopRepository laptopRepository;
	private final BrandRepository brandRepository;

	public LaptopService(LaptopRepository laptopRepository, BrandRepository brandRepository) {
		this.laptopRepository = laptopRepository;
		this.brandRepository = brandRepository;
	}

	public LaptopResponse createLaptop(LaptopCreateRequest request) {

		Brand brand = brandRepository.findByIdAndDeletedAtIsNull(request.getBrandId()).orElseThrow(
				() -> new ApiException(ApiErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND, "Brand not found"));

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

		Laptop saved = laptopRepository.save(laptop);
		return toResponse(saved);
	}

	@Transactional(readOnly = true)
	public Page<LaptopResponse> getLaptops(Pageable pageable) {
		return laptopRepository.findAllByDeletedAtIsNull(pageable).map(this::toResponse);
	}

	private LaptopResponse toResponse(Laptop laptop) {
		return new LaptopResponse(laptop.getId(), laptop.getBrand().getName(), laptop.getModelName(),
				laptop.getEffectivePrice(), laptop.getStock(), laptop.getStatus());
	}
}
