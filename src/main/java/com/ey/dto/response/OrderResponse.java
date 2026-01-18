package com.ey.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.ey.enums.OrderStatus;

public class OrderResponse {

	private Long orderId;
	private OrderStatus status;
	private BigDecimal totalAmount;
	private Instant createdAt;
	private List<OrderItemResponse> items;

	public OrderResponse(Long orderId, OrderStatus status, BigDecimal totalAmount, Instant createdAt,
			List<OrderItemResponse> items) {
		this.orderId = orderId;
		this.status = status;
		this.totalAmount = totalAmount;
		this.createdAt = createdAt;
		this.items = items;
	}

	public Long getOrderId() {
		return orderId;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}
}
