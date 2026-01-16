package com.ey.dto.response;

import java.math.BigDecimal;

public class CartItemResponse {

	private Long laptopId;
	private String modelName;
	private int quantity;
	private BigDecimal priceAtAdd;
	private BigDecimal subTotal;

	public CartItemResponse(Long laptopId, String modelName, int quantity, BigDecimal priceAtAdd, BigDecimal subTotal) {
		this.laptopId = laptopId;
		this.modelName = modelName;
		this.quantity = quantity;
		this.priceAtAdd = priceAtAdd;
		this.subTotal = subTotal;
	}

	public Long getLaptopId() {
		return laptopId;
	}

	public String getModelName() {
		return modelName;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getPriceAtAdd() {
		return priceAtAdd;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}
}
