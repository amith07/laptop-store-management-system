package com.ey.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ey.dto.response.PaymentResponse;
import com.ey.enums.ApiErrorCode;
import com.ey.enums.OrderStatus;
import com.ey.enums.PaymentStatus;
import com.ey.exception.ApiException;
import com.ey.model.Order;
import com.ey.model.Payment;
import com.ey.repository.OrderRepository;
import com.ey.repository.PaymentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentService {

	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {

		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
	}

	public PaymentResponse pay(String username, Long orderId) {

		log.info("Payment request received for orderId={} by user={}", orderId, username);

		Order order = orderRepository.findById(orderId).orElseThrow(
				() -> new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found"));

		if (!order.getUsername().equals(username)) {
			log.warn("Unauthorized payment attempt: user={} orderId={}", username, orderId);
			throw new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found");
		}

		if (order.getStatus() != OrderStatus.CREATED) {
			log.warn("Invalid payment request: orderId={} status={}", orderId, order.getStatus());
			throw new ApiException(ApiErrorCode.INVALID_PAYMENT_REQUEST, HttpStatus.CONFLICT,
					"Order not eligible for payment");
		}

		paymentRepository.findByOrderId(orderId).ifPresent(p -> {
			log.warn("Payment already processed for orderId={}", orderId);
			throw new ApiException(ApiErrorCode.PAYMENT_ALREADY_PROCESSED, HttpStatus.CONFLICT,
					"Payment already processed");
		});

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setAmount(order.getTotalAmount());
		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setPaidAt(Instant.now());

		Payment saved = paymentRepository.save(payment);

		// ✅ Payment successful → complete order
		order.setStatus(OrderStatus.COMPLETED);
		orderRepository.save(order);

		log.info("Payment {} successful for orderId={}", saved.getId(), orderId);

		return new PaymentResponse(saved.getId(), orderId, saved.getStatus(), saved.getAmount(), saved.getPaidAt());
	}
}
