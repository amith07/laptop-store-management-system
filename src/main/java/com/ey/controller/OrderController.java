package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.OrderResponse;
import com.ey.service.OrderService;

@RestController
@RequestMapping(path = { "/api/orders", "/api/orders/" })
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	private String currentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

	@GetMapping(path = { "", "/" })
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<List<OrderResponse>> getOrders() {
		return ResponseEntity.ok(orderService.getOrders(currentUser()));
	}

	@PostMapping(path = { "/checkout", "/checkout/" })
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<OrderResponse> checkout() {
		return ResponseEntity.ok(orderService.checkout(currentUser()));
	}

	@PostMapping(path = { "/{orderId}/cancel", "/{orderId}/cancel/" })
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.cancelOrder(currentUser(), orderId));
	}
}
