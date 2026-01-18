package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.enums.OrderStatus;
import com.ey.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUsernameOrderByCreatedAtDesc(String username);

	List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

	List<Order> findAllByOrderByCreatedAtDesc();
}
