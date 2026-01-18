package com.ey.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ey.dto.response.PaymentResponse;
import com.ey.enums.ApiErrorCode;
import com.ey.enums.OrderStatus;
import com.ey.enums.PaymentStatus;
import com.ey.exception.ApiException;
import com.ey.model.Order;
import com.ey.model.Payment;
import com.ey.repository.OrderRepository;
import com.ey.repository.PaymentRepository;

@Service
@Transactional
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
	}

	public PaymentResponse pay(String username, Long orderId) {

		Order order = orderRepository.findById(orderId).orElseThrow(
				() -> new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found"));

		if (!order.getUsername().equals(username)) {
			throw new ApiException(ApiErrorCode.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND, "Order not found");
		}

		if (order.getStatus() != OrderStatus.COMPLETED) {
			throw new ApiException(ApiErrorCode.INVALID_PAYMENT_REQUEST, HttpStatus.CONFLICT,
					"Order not eligible for payment");
		}

		paymentRepository.findByOrderId(orderId).ifPresent(p -> {
			throw new ApiException(ApiErrorCode.PAYMENT_ALREADY_PROCESSED, HttpStatus.CONFLICT,
					"Payment already completed");
		});

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setAmount(order.getTotalAmount());
		payment.setStatus(PaymentStatus.SUCCESS);

		Payment saved = paymentRepository.save(payment);

		return new PaymentResponse(saved.getId(), orderId, saved.getStatus(), saved.getAmount(), saved.getPaidAt());
	}
}
