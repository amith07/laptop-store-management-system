package com.ey.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LaptopCreateRequest {

	@NotNull
	private Long brandId;

	@NotBlank
	private String modelName;

	@NotBlank
	private String serialNumber;

	@NotBlank
	private String cpu;

	@Min(1)
	private int ramGb;

	@Min(1)
	private int storageGb;

	@DecimalMin("0.1")
	private BigDecimal screenSizeInches;

	@DecimalMin("0.01")
	private BigDecimal price;

	private BigDecimal discountPercentage;
	private BigDecimal promotionalPrice;

	@Min(0)
	private int stock;

	public LaptopCreateRequest(@NotNull Long brandId, @NotBlank String modelName, @NotBlank String serialNumber,
			@NotBlank String cpu, @Min(1) int ramGb, @Min(1) int storageGb,
			@DecimalMin("0.1") BigDecimal screenSizeInches, @DecimalMin("0.01") BigDecimal price,
			BigDecimal discountPercentage, BigDecimal promotionalPrice, @Min(0) int stock) {
		super();
		this.brandId = brandId;
		this.modelName = modelName;
		this.serialNumber = serialNumber;
		this.cpu = cpu;
		this.ramGb = ramGb;
		this.storageGb = storageGb;
		this.screenSizeInches = screenSizeInches;
		this.price = price;
		this.discountPercentage = discountPercentage;
		this.promotionalPrice = promotionalPrice;
		this.stock = stock;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public int getRamGb() {
		return ramGb;
	}

	public void setRamGb(int ramGb) {
		this.ramGb = ramGb;
	}

	public int getStorageGb() {
		return storageGb;
	}

	public void setStorageGb(int storageGb) {
		this.storageGb = storageGb;
	}

	public BigDecimal getScreenSizeInches() {
		return screenSizeInches;
	}

	public void setScreenSizeInches(BigDecimal screenSizeInches) {
		this.screenSizeInches = screenSizeInches;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public BigDecimal getPromotionalPrice() {
		return promotionalPrice;
	}

	public void setPromotionalPrice(BigDecimal promotionalPrice) {
		this.promotionalPrice = promotionalPrice;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

}
