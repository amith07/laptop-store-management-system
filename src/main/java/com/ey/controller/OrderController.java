package com.ey.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	private String currentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> myOrders() {
		return ResponseEntity.ok(orderService.getOrders(currentUser()));
	}

	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder() {
		return new ResponseEntity<>(orderService.checkout(currentUser()), HttpStatus.CREATED);
	}

	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponse> cancel(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.cancelOrder(currentUser(), orderId));
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<?> getOrdersByStatus(@PathVariable String status) {
		return ResponseEntity.ok(orderService.getOrdersByStatus(currentUser(), status));
	}

}
