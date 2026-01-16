package com.ey.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {

	private Long cartId;
	private String username;
	private BigDecimal totalAmount;
	private List<CartItemResponse> items;

	public CartResponse(Long cartId, String username, BigDecimal totalAmount, List<CartItemResponse> items) {
		this.cartId = cartId;
		this.username = username;
		this.totalAmount = totalAmount;
		this.items = items;
	}

	public Long getCartId() {
		return cartId;
	}

	public String getUsername() {
		return username;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public List<CartItemResponse> getItems() {
		return items;
	}
}
