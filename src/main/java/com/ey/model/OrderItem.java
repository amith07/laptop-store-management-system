package com.ey.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(optional = false)
	@JoinColumn(name = "laptop_id")
	private Laptop laptop;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal priceAtPurchase;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal subTotal;

	/*
	 * ============================ Getters ============================
	 */

	public Long getId() {
		return id;
	}

	public Order getOrder() {
		return order;
	}

	public Laptop getLaptop() {
		return laptop;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getPriceAtPurchase() {
		return priceAtPurchase;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	/*
	 * ============================ Setters ============================
	 */

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setLaptop(Laptop laptop) {
		this.laptop = laptop;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
		this.priceAtPurchase = priceAtPurchase;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
}
