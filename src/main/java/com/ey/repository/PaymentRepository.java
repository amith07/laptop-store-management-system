package com.ey.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Optional<Payment> findByOrderId(Long orderId);
}
