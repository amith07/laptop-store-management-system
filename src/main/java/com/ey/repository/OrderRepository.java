package com.ey.repository;

import com.ey.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUsernameOrderByCreatedAtDesc(String username);
}
