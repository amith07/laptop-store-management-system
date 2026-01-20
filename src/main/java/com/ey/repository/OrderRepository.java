package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ey.enums.OrderStatus;
import com.ey.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUsernameOrderByCreatedAtDesc(String username);

	List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

	List<Order> findAllByOrderByCreatedAtDesc();
	
	List<Order> findByUsernameAndStatus(String username, OrderStatus status);
	
	List<Order> findByStatus(OrderStatus status);

	@Query("""
		select o from Order o
		where date(o.createdAt) = current_date
	""")
	List<Order> findTodayOrders();

}
