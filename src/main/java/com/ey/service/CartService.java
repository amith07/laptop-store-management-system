package com.ey.service;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ey.dto.request.AddToCartRequest;
import com.ey.dto.response.CartItemResponse;
import com.ey.dto.response.CartResponse;
import com.ey.enums.ApiErrorCode;
import com.ey.exception.ApiException;
import com.ey.model.Cart;
import com.ey.model.CartItem;
import com.ey.model.Laptop;
import com.ey.repository.CartRepository;
import com.ey.repository.LaptopRepository;

@Service
@Transactional
public class CartService {

	private final CartRepository cartRepository;
	private final LaptopRepository laptopRepository;

	public CartService(CartRepository cartRepository, LaptopRepository laptopRepository) {
		this.cartRepository = cartRepository;
		this.laptopRepository = laptopRepository;
	}

	public CartResponse addToCart(String username, AddToCartRequest request) {

		Laptop laptop = laptopRepository.findByIdAndDeletedAtIsNull(request.getLaptopId()).orElseThrow(
				() -> new ApiException(ApiErrorCode.LAPTOP_NOT_FOUND, HttpStatus.NOT_FOUND, "Laptop not found"));

		if (laptop.getStock() < request.getQuantity()) {
			throw new ApiException(ApiErrorCode.INSUFFICIENT_STOCK, HttpStatus.BAD_REQUEST, "Not enough stock");
		}

		Cart cart = cartRepository.findByUsernameAndCheckedOutFalse(username).orElseGet(() -> {
			Cart c = new Cart();
			c.setUsername(username);
			return cartRepository.save(c);
		});

		CartItem item = cart.getItems().stream().filter(ci -> ci.getLaptop().getId().equals(laptop.getId())).findFirst()
				.orElse(null);

		if (item == null) {
			item = new CartItem();
			item.setCart(cart);
			item.setLaptop(laptop);
			item.setPriceAtAdd(laptop.getEffectivePrice());
			item.setQuantity(request.getQuantity());
			item.recalculate();
			cart.getItems().add(item);
		} else {
			item.setQuantity(item.getQuantity() + request.getQuantity());
			item.recalculate();
		}

		cart.recalculateTotal();
		return toResponse(cart);
	}

	@Transactional(readOnly = true)
	public CartResponse getCart(String username) {

		Cart cart = cartRepository.findByUsernameAndCheckedOutFalse(username)
				.orElseThrow(() -> new ApiException(ApiErrorCode.CART_EMPTY, HttpStatus.NOT_FOUND, "Cart is empty"));

		return toResponse(cart);
	}

	private CartResponse toResponse(Cart cart) {
		return new CartResponse(cart.getId(), cart.getUsername(), cart.getTotalAmount(),
				cart.getItems().stream().map(i -> new CartItemResponse(i.getLaptop().getId(),
						i.getLaptop().getModelName(), i.getQuantity(), i.getPriceAtAdd(), i.getSubTotal()))
						.collect(Collectors.toList()));
	}
}
