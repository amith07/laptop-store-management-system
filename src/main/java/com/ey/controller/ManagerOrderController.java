package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.OrderResponse;
import com.ey.enums.OrderStatus;
import com.ey.service.OrderService;

@RestController
@RequestMapping("/api/manager/orders")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class ManagerOrderController {

	private final OrderService orderService;

	public ManagerOrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable OrderStatus status) {

		return ResponseEntity.ok(orderService.getOrdersByStatus(status));
	}
}
