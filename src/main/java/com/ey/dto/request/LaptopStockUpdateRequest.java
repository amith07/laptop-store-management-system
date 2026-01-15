package com.ey.dto.request;

import jakarta.validation.constraints.Min;

public class LaptopStockUpdateRequest {

	@Min(0)
	private int stock;

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}
