package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.OrderResponse;
import com.ey.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	// TEMP mocked user
	private String currentUser() {
		return "customer1";
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/checkout")
	public ResponseEntity<OrderResponse> checkout() {
		return ResponseEntity.ok(orderService.checkout(currentUser()));
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping
	public ResponseEntity<List<OrderResponse>> getOrders() {
		return ResponseEntity.ok(orderService.getOrders(currentUser()));
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.cancelOrder(currentUser(), orderId));
	}

}
