package com.ey.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public class LaptopUpdateRequest {

	private String modelName;
	private String cpu;

	@Min(1)
	private Integer ramGb;

	@Min(1)
	private Integer storageGb;

	@DecimalMin("0.1")
	private BigDecimal screenSizeInches;

	@DecimalMin("0.01")
	private BigDecimal price;

	private BigDecimal discountPercentage;
	private BigDecimal promotionalPrice;

	/* Getters & Setters */

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public Integer getRamGb() {
		return ramGb;
	}

	public void setRamGb(Integer ramGb) {
		this.ramGb = ramGb;
	}

	public Integer getStorageGb() {
		return storageGb;
	}

	public void setStorageGb(Integer storageGb) {
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
}
