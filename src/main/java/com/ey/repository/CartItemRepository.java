package com.ey.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
