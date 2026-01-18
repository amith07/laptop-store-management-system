package com.ey.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

	/**
	 * Extract authenticated username from SecurityContext
	 */
	private String currentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	/**
	 * CUSTOMER – View own orders
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping
	public ResponseEntity<List<OrderResponse>> myOrders() {
		return ResponseEntity.ok(orderService.getOrders(currentUser()));
	}

	/**
	 * CUSTOMER – Checkout cart (PRIMARY ORDER CREATION) POST /api/orders
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder() {
		return new ResponseEntity<>(orderService.checkout(currentUser()), HttpStatus.CREATED);
	}

	/**
	 * CUSTOMER – Cancel order
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponse> cancel(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.cancelOrder(currentUser(), orderId));
	}
}
