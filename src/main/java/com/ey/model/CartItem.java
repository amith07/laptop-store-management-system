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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "cart_items", uniqueConstraints = {
		@UniqueConstraint(name = "uk_cart_laptop", columnNames = { "cart_id", "laptop_id" }) })
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@ManyToOne(optional = false)
	@JoinColumn(name = "laptop_id")
	private Laptop laptop;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal priceAtAdd;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal subTotal;

	/*
	 * ============================ Business Logic ============================
	 */
	public void recalculate() {
		this.subTotal = priceAtAdd.multiply(BigDecimal.valueOf(quantity));
	}

	/*
	 * ============================ Getters & Setters ============================
	 */
	public Long getId() {
		return id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Laptop getLaptop() {
		return laptop;
	}

	public void setLaptop(Laptop laptop) {
		this.laptop = laptop;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPriceAtAdd() {
		return priceAtAdd;
	}

	public void setPriceAtAdd(BigDecimal priceAtAdd) {
		this.priceAtAdd = priceAtAdd;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}
}
