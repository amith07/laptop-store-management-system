package com.ey.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ey.service.OrderService;

@RestController
@RequestMapping("/api/manager/orders")
public class ManagerOrderController {

	private final OrderService orderService;

	public ManagerOrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/pending")
	public ResponseEntity<?> pendingOrders() {
		return ResponseEntity.ok(orderService.getPendingOrders());
	}

	@GetMapping("/today")
	public ResponseEntity<?> todaysOrders() {
		return ResponseEntity.ok(orderService.getTodaysOrders());
	}
}
