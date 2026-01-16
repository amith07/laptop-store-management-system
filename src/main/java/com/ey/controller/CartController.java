package com.ey.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.AddToCartRequest;
import com.ey.dto.response.CartResponse;
import com.ey.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	// TEMP: username is mocked
	private String currentUser() {
		return "customer1";
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/items")
	public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody AddToCartRequest request) {
		return ResponseEntity.ok(cartService.addToCart(currentUser(), request));
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping
	public ResponseEntity<CartResponse> getCart() {
		return ResponseEntity.ok(cartService.getCart(currentUser()));
	}
}
