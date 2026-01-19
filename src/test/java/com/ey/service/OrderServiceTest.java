package com.ey.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private CartRepository cartRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private LaptopRepository laptopRepository;

	@InjectMocks
	private OrderService orderService;

	private Cart cart;
	private Laptop laptop;
	private CartItem cartItem;

	private static final String USERNAME = "customer";

	@BeforeEach
	void setup() {

		laptop = new Laptop();
		laptop.setModelName("Dell XPS");
		laptop.setStock(5);

		cartItem = new CartItem();
		cartItem.setLaptop(laptop);
		cartItem.setQuantity(2);
		cartItem.setPriceAtAdd(BigDecimal.valueOf(1000));

		cart = new Cart();
		cart.setUsername(USERNAME);
		cart.setCheckedOut(false);
		cart.setTotalAmount(BigDecimal.valueOf(2000)); // ✅ FIX

		cart.getItems().add(cartItem);
	}

	/* ===================== CHECKOUT ===================== */

	@Test
	void checkout_success() {

		when(cartRepository.findByUsernameAndCheckedOutFalse(USERNAME)).thenReturn(Optional.of(cart));

		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

		OrderResponse response = orderService.checkout(USERNAME);

		assertNotNull(response);
		assertEquals(OrderStatus.COMPLETED, response.getStatus());
		assertEquals(BigDecimal.valueOf(2000), response.getTotalAmount());

		verify(laptopRepository).save(laptop);
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	void checkout_cartEmpty_shouldThrowException() {

		when(cartRepository.findByUsernameAndCheckedOutFalse(USERNAME)).thenReturn(Optional.empty());

		ApiException ex = assertThrows(ApiException.class, () -> orderService.checkout(USERNAME));

		assertEquals(ApiErrorCode.CART_EMPTY, ex.getErrorCode());
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
	}

	@Test
	void checkout_insufficientStock_shouldThrowException() {

		laptop.setStock(1); // less than requested

		when(cartRepository.findByUsernameAndCheckedOutFalse(USERNAME)).thenReturn(Optional.of(cart));

		ApiException ex = assertThrows(ApiException.class, () -> orderService.checkout(USERNAME));

		assertEquals(ApiErrorCode.INSUFFICIENT_STOCK, ex.getErrorCode());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());

		verify(orderRepository, never()).save(any());
	}

	/* ===================== CANCEL ORDER ===================== */

	@Test
	void cancelOrder_success() {

		Order order = new Order();
		order.setUsername(USERNAME);
		order.setStatus(OrderStatus.COMPLETED);

		OrderItem orderItem = new OrderItem();
		orderItem.setLaptop(laptop);
		orderItem.setQuantity(2);

		order.getItems().add(orderItem);

		when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

		OrderResponse response = orderService.cancelOrder(USERNAME, 10L);

		assertEquals(OrderStatus.CANCELLED, response.getStatus());
		verify(laptopRepository).save(laptop);
	}

	@Test
	void cancelOrder_notOwner_shouldFail() {

		Order order = new Order();
		order.setUsername("otherUser");
		order.setStatus(OrderStatus.COMPLETED);

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		ApiException ex = assertThrows(ApiException.class, () -> orderService.cancelOrder(USERNAME, 1L));

		assertEquals(ApiErrorCode.ORDER_NOT_FOUND, ex.getErrorCode());
	}

	@Test
	void cancelOrder_invalidStatus_shouldFail() {

		Order order = new Order();
		order.setUsername(USERNAME);
		order.setStatus(OrderStatus.CREATED);

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		ApiException ex = assertThrows(ApiException.class, () -> orderService.cancelOrder(USERNAME, 1L));

		assertEquals(ApiErrorCode.ORDER_CANNOT_BE_CANCELLED, ex.getErrorCode());
		assertEquals(HttpStatus.CONFLICT, ex.getStatus());
	}
}
