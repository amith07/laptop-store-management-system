package com.ey.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUsernameAndCheckedOutFalse(String username);
}
