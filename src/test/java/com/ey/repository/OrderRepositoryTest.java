package com.ey.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.ey.enums.OrderStatus;
import com.ey.model.Order;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@BeforeEach
	void cleanDatabase() {
		orderRepository.deleteAll();
	}

	@Test
	void findByUsernameOrderByCreatedAtDesc_shouldReturnLatestFirst() {

		Order firstOrder = new Order();
		firstOrder.setUsername("customer");
		firstOrder.setStatus(OrderStatus.COMPLETED);
		firstOrder.setTotalAmount(BigDecimal.valueOf(1000));

		Order secondOrder = new Order();
		secondOrder.setUsername("customer");
		secondOrder.setStatus(OrderStatus.COMPLETED);
		secondOrder.setTotalAmount(BigDecimal.valueOf(2000));

		orderRepository.save(firstOrder);
		orderRepository.save(secondOrder);

		List<Order> orders = orderRepository.findByUsernameOrderByCreatedAtDesc("customer");

		assertEquals(2, orders.size());
		assertEquals(BigDecimal.valueOf(2000), orders.get(0).getTotalAmount());
	}

	@Test
	void findByStatusOrderByCreatedAtDesc_shouldFilterCorrectly() {

		Order completed = new Order();
		completed.setUsername("customer");
		completed.setStatus(OrderStatus.COMPLETED);
		completed.setTotalAmount(BigDecimal.valueOf(1500));

		Order cancelled = new Order();
		cancelled.setUsername("customer");
		cancelled.setStatus(OrderStatus.CANCELLED);
		cancelled.setTotalAmount(BigDecimal.valueOf(500));

		orderRepository.save(completed);
		orderRepository.save(cancelled);

		List<Order> completedOrders = orderRepository.findByStatusOrderByCreatedAtDesc(OrderStatus.COMPLETED);

		assertEquals(1, completedOrders.size());
		assertEquals(OrderStatus.COMPLETED, completedOrders.get(0).getStatus());
	}

	@Test
	void findAllByOrderByCreatedAtDesc_shouldReturnAllOrders() {

		Order first = new Order();
		first.setUsername("a");
		first.setStatus(OrderStatus.COMPLETED);
		first.setTotalAmount(BigDecimal.valueOf(100));

		Order second = new Order();
		second.setUsername("b");
		second.setStatus(OrderStatus.COMPLETED);
		second.setTotalAmount(BigDecimal.valueOf(200));

		orderRepository.save(first);
		orderRepository.save(second);

		List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();

		assertEquals(2, orders.size());
	}
}
