package com.ey.dto.response;

import java.math.BigDecimal;

import com.ey.enums.LaptopStatus;

public class LaptopResponse {

	private Long id;
	private String brandName;
	private String modelName;
	private BigDecimal effectivePrice;
	private int stock;
	private LaptopStatus status;

	public LaptopResponse(Long id, String brandName, String modelName, BigDecimal effectivePrice, int stock,
			LaptopStatus status) {
		this.id = id;
		this.brandName = brandName;
		this.modelName = modelName;
		this.effectivePrice = effectivePrice;
		this.stock = stock;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getBrandName() {
		return brandName;
	}

	public String getModelName() {
		return modelName;
	}

	public BigDecimal getEffectivePrice() {
		return effectivePrice;
	}

	public int getStock() {
		return stock;
	}

	public LaptopStatus getStatus() {
		return status;
	}
}
