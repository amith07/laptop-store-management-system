package com.ey.dto.response;

import java.math.BigDecimal;

public class OrderItemResponse {

	private Long laptopId;
	private String modelName;
	private int quantity;
	private BigDecimal priceAtPurchase;
	private BigDecimal subTotal;

	public OrderItemResponse(Long laptopId, String modelName, int quantity, BigDecimal priceAtPurchase,
			BigDecimal subTotal) {
		this.laptopId = laptopId;
		this.modelName = modelName;
		this.quantity = quantity;
		this.priceAtPurchase = priceAtPurchase;
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

	public BigDecimal getPriceAtPurchase() {
		return priceAtPurchase;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}
}
