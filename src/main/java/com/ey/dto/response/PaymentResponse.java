package com.ey.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.ey.enums.PaymentStatus;

public class PaymentResponse {

	private Long paymentId;
	private Long orderId;
	private PaymentStatus status;
	private BigDecimal amount;
	private Instant paidAt;
	private Instant refundedAt;

	public PaymentResponse(Long paymentId, Long orderId, PaymentStatus status, BigDecimal amount, Instant paidAt,
			Instant refundedAt) {

		this.paymentId = paymentId;
		this.orderId = orderId;
		this.status = status;
		this.amount = amount;
		this.paidAt = paidAt;
		this.refundedAt = refundedAt;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Instant getPaidAt() {
		return paidAt;
	}

	public Instant getRefundedAt() {
		return refundedAt;
	}
}
