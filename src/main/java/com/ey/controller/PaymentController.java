package com.ey.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.PaymentResponse;
import com.ey.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	private String currentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/orders/{orderId}")
	public ResponseEntity<PaymentResponse> pay(@PathVariable Long orderId) {
		return ResponseEntity.ok(paymentService.pay(currentUser(), orderId));
	}
}
