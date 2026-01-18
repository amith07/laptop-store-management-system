package com.ey.service;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ey.dto.response.OrderItemResponse;
import com.ey.dto.response.OrderResponse;
import com.ey.enums.ApiErrorCode;
import com.ey.enums.OrderStatus;
import com.ey.exception.ApiException;
import com.ey.model.Cart;
import com.ey.model.CartItem;
import com.ey.model.Laptop;
import com.ey.model.Order;
import com.ey.model.OrderItem;
import com.ey.repository.CartRepository;
import com.ey.repository.LaptopRepository;
import com.ey.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final LaptopRepository laptopRepository;

	public OrderService(CartRepository cartRepository, OrderRepository orderRepository,
			LaptopRepository laptopRepository) {
		this.cartRepository = cartRepository;
		this.orderRepository = orderRepository;
		this.laptopRepository = laptopRepository;
	}

	/* ============================ CHECKOUT ============================ */

	public OrderResponse checkout(String username) {

		Cart cart = cartRepository.findByUsernameAndCheckedOutFalse(username)
				.orElseThrow(() -> new ApiException(ApiErrorCode.CART_EMPTY, HttpStatus.NOT_FOUND, "Cart is empty"));

		if (cart.getItems().isEmpty()) {
			throw new ApiException(ApiErrorCode.CART_EMPTY, HttpStatus.NOT_FOUND, "Cart is empty");
		}

		Order order = new Order();
		order.setUsername(username);
		order.setStatus(OrderStatus.CREATED);
		order.setTotalAmount(cart.getTotalAmount());

		for (CartItem cartItem : cart.getItems()) {

			Laptop laptop = cartItem.getLaptop();

			if (laptop.getStock() < cartItem.getQuantity()) {
				throw new ApiException(ApiErrorCode.INSUFFICIENT_STOCK, HttpStatus.BAD_REQUEST,
						"Not enough stock during checkout");
			}

			// Reduce stock
			laptop.setStock(laptop.getStock() - cartItem.getQuantity());
			laptopRepository.save(laptop);

			OrderItem item = new OrderItem();
			item.setOrder(order);
			item.setLaptop(laptop);
			item.setQuantity(cartItem.getQuantity());
			item.setPriceAtPurchase(cartItem.getPriceAtAdd());
			item.setSubTotal(cartItem.getSubTotal());

			order.getItems().add(item);
		}

		cart.setCheckedOut(true);

		// Payment successful → order completed
		order.setStatus(OrderStatus.COMPLETED);

		return toResponse(orderRepository.save(order));
	}

	/* ============================ CANCEL ORDER ============================ */

	public OrderResponse cancelOrder(String username, Long orderId) {

		Order order = orderRepository.findById(orderId).orElseThrow(
				() -> new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found"));

		if (!order.getUsername().equals(username)) {
			throw new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found");
		}

		if (order.getStatus() != OrderStatus.COMPLETED) {
			throw new ApiException(ApiErrorCode.ORDER_CANNOT_BE_CANCELLED, HttpStatus.CONFLICT,
					"Order cannot be cancelled");
		}

		// Restore stock
		order.getItems().forEach(item -> {
			Laptop laptop = item.getLaptop();
			laptop.setStock(laptop.getStock() + item.getQuantity());
			laptopRepository.save(laptop);
		});

		order.setStatus(OrderStatus.CANCELLED);

		return toResponse(order);
	}

	/* ============================ ORDER HISTORY ============================ */

	@Transactional(readOnly = true)
	public java.util.List<OrderResponse> getOrders(String username) {

		return orderRepository.findByUsernameOrderByCreatedAtDesc(username).stream().map(this::toResponse)
				.collect(Collectors.toList());
	}

	/* ============================ MAPPER ============================ */

	private OrderResponse toResponse(Order order) {

		return new OrderResponse(order.getId(), order.getStatus(), order.getTotalAmount(), order.getCreatedAt(),
				order.getItems().stream()
						.map(i -> new OrderItemResponse(i.getLaptop().getId(), i.getLaptop().getModelName(),
								i.getQuantity(), i.getPriceAtPurchase(), i.getSubTotal()))
						.collect(Collectors.toList()));
	}

	/* ============================ ADMIN ============================ */

	@Transactional(readOnly = true)
	public java.util.List<OrderResponse> getAllOrders() {

		return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse)
				.collect(java.util.stream.Collectors.toList());
	}

	/* ============================ MANAGER ============================ */

	@Transactional(readOnly = true)
	public java.util.List<OrderResponse> getOrdersByStatus(OrderStatus status) {

		return orderRepository.findByStatusOrderByCreatedAtDesc(status).stream().map(this::toResponse)
				.collect(java.util.stream.Collectors.toList());
	}

}
