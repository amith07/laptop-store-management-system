package com.ey.dto.request;

import java.math.BigDecimal;

import com.ey.enums.LaptopStatus;

public class LaptopSearchRequest {

	private Long brandId;
	private String cpu;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private LaptopStatus status;

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public LaptopStatus getStatus() {
		return status;
	}

	public void setStatus(LaptopStatus status) {
		this.status = status;
	}
}
