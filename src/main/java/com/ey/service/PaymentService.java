package com.ey.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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

		order.setStatus(OrderStatus.COMPLETED);

		Payment saved = paymentRepository.save(payment);

		log.info("Payment {} successful for orderId={}", saved.getId(), orderId);

		return toResponse(saved);
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

		// restore stock
		order.getItems().forEach(item -> {
			Laptop laptop = item.getLaptop();
			laptop.setStock(laptop.getStock() + item.getQuantity());
		});

		payment.setStatus(PaymentStatus.REFUNDED);
		payment.setRefundedAt(Instant.now());
		order.setStatus(OrderStatus.CANCELLED);

		log.info("Payment {} refunded successfully", paymentId);

		return toResponse(payment);
	}

	/* ============================ PAYMENT HISTORY ============================ */

	@Transactional
	public List<PaymentResponse> getMyPayments(String username) {

		log.info("Fetching payment history for user={}", username);

		return paymentRepository.findByOrderUsernameOrderByPaidAtDesc(username).stream().map(this::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public List<PaymentResponse> getAllPayments() {

		log.info("Fetching all payments (admin)");

		return paymentRepository.findAllByOrderByPaidAtDesc().stream().map(this::toResponse)
				.collect(Collectors.toList());
	}

	private PaymentResponse toResponse(Payment payment) {

		return new PaymentResponse(payment.getId(), payment.getOrder().getId(), payment.getStatus(),
				payment.getAmount(), payment.getPaidAt(), payment.getRefundedAt());
	}
}
