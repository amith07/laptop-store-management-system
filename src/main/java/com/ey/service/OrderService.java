package com.ey.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

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

		log.info("Starting checkout for user={}", username);

		Cart cart = cartRepository.findByUsernameAndCheckedOutFalse(username).orElseThrow(() -> {
			log.warn("Checkout failed: cart empty for user={}", username);
			return new ApiException(ApiErrorCode.CART_EMPTY, HttpStatus.NOT_FOUND, "Cart is empty");
		});

		if (cart.getItems().isEmpty()) {
			log.warn("Checkout failed: cart has no items for user={}", username);
			throw new ApiException(ApiErrorCode.CART_EMPTY, HttpStatus.NOT_FOUND, "Cart is empty");
		}

		Order order = new Order();
		order.setUsername(username);
		order.setStatus(OrderStatus.CREATED);
		order.setTotalAmount(cart.getTotalAmount());

		for (CartItem cartItem : cart.getItems()) {

			Laptop laptop = cartItem.getLaptop();

			if (laptop.getStock() < cartItem.getQuantity()) {
				log.warn("Insufficient stock: laptopId={}, requested={}, available={}", laptop.getId(),
						cartItem.getQuantity(), laptop.getStock());
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

		Order savedOrder = orderRepository.save(order);

		log.info("Order {} created successfully for user={}", savedOrder.getId(), username);

		return toResponse(savedOrder);
	}

	/* ============================ CANCEL ORDER ============================ */

	public OrderResponse cancelOrder(String username, Long orderId) {

		log.info("Cancel request received for orderId={} by user={}", orderId, username);

		Order order = orderRepository.findById(orderId).orElseThrow(
				() -> new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found"));

		if (!order.getUsername().equals(username)) {
			log.warn("Unauthorized cancel attempt: user={} orderId={}", username, orderId);
			throw new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found");
		}

		if (order.getStatus() != OrderStatus.CREATED) {
			log.warn("Order cannot be cancelled: orderId={} status={}", orderId, order.getStatus());
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

		log.info("Order {} cancelled successfully by user={}", orderId, username);

		return toResponse(order);
	}

	/* ============================ ORDER HISTORY ============================ */

	@Transactional(readOnly = true)
	public List<OrderResponse> getOrders(String username) {

		log.info("Fetching orders for user={}", username);

		return orderRepository.findByUsernameOrderByCreatedAtDesc(username).stream().map(this::toResponse)
				.collect(Collectors.toList());
	}

	public List<Order> getOrdersByStatus(String username, String status) {
		return orderRepository.findByUsernameAndStatus(username, OrderStatus.valueOf(status));
	}

	public List<Order> getPendingOrders() {
		return orderRepository.findByStatus(OrderStatus.CREATED);
	}

	public List<Order> getTodaysOrders() {
		return orderRepository.findTodayOrders();
	}

	/* ============================ ADMIN ============================ */

	@Transactional(readOnly = true)
	public List<OrderResponse> getAllOrders() {

		log.info("Fetching all orders (admin)");

		return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse)
				.collect(Collectors.toList());
	}

	/* ============================ MANAGER ============================ */

	@Transactional(readOnly = true)
	public List<OrderResponse> getOrdersByStatus(OrderStatus status) {

		log.info("Fetching orders by status={}", status);

		return orderRepository.findByStatusOrderByCreatedAtDesc(status).stream().map(this::toResponse)
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
}
