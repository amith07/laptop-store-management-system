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
import com.ey.model.Laptop;
import com.ey.model.Order;
import com.ey.model.Payment;
import com.ey.repository.LaptopRepository;
import com.ey.repository.OrderRepository;
import com.ey.repository.PaymentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentService {

	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final LaptopRepository laptopRepository;

	public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository,
			LaptopRepository laptopRepository) {

		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
		this.laptopRepository = laptopRepository;
	}

	/* ============================ PAY ============================ */

	public PaymentResponse pay(String username, Long orderId) {

		log.info("Payment request received for orderId={} by user={}", orderId, username);

		Order order = orderRepository.findById(orderId).orElseThrow(
				() -> new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found"));

		if (!order.getUsername().equals(username)) {
			throw new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found");
		}

		if (order.getStatus() != OrderStatus.CREATED) {
			throw new ApiException(ApiErrorCode.INVALID_PAYMENT_REQUEST, HttpStatus.CONFLICT,
					"Order not eligible for payment");
		}

		paymentRepository.findByOrderId(orderId).ifPresent(p -> {
			throw new ApiException(ApiErrorCode.PAYMENT_ALREADY_PROCESSED, HttpStatus.CONFLICT,
					"Payment already processed");
		});

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setAmount(order.getTotalAmount());
		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setPaidAt(Instant.now());

		Payment savedPayment = paymentRepository.save(payment);

		// Persist order state
		order.setStatus(OrderStatus.COMPLETED);
		orderRepository.save(order);

		log.info("Payment {} successful for orderId={}", savedPayment.getId(), orderId);

		return toResponse(savedPayment);
	}

	/* ============================ REFUND ============================ */

	public PaymentResponse refund(String username, Long paymentId) {

		log.info("Refund request received for paymentId={} by user={}", paymentId, username);

		Payment payment = paymentRepository.findById(paymentId).orElseThrow(
				() -> new ApiException(ApiErrorCode.PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND, "Payment not found"));

		Order order = payment.getOrder();

		if (!order.getUsername().equals(username)) {
			throw new ApiException(ApiErrorCode.PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND, "Payment not found");
		}

		if (payment.getStatus() != PaymentStatus.SUCCESS) {
			throw new ApiException(ApiErrorCode.INVALID_REFUND_REQUEST, HttpStatus.CONFLICT,
					"Payment cannot be refunded");
		}

		// Restore stock (persist laptops)
		order.getItems().forEach(item -> {
			Laptop laptop = item.getLaptop();
			laptop.setStock(laptop.getStock() + item.getQuantity());
			laptopRepository.save(laptop);
		});

		// Update payment
		payment.setStatus(PaymentStatus.REFUNDED);
		payment.setRefundedAt(Instant.now());
		paymentRepository.save(payment);

		// Update order
		order.setStatus(OrderStatus.CANCELLED);
		orderRepository.save(order);

		log.info("Payment {} refunded successfully", paymentId);

		return toResponse(payment);
	}

	/* ============================ MAPPER ============================ */

	private PaymentResponse toResponse(Payment payment) {
		return new PaymentResponse(payment.getId(), payment.getOrder().getId(), payment.getStatus(),
				payment.getAmount(), payment.getPaidAt(), payment.getRefundedAt());
	}
}
