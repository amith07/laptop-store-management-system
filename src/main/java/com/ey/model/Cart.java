package com.ey.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// TEMP: user identifier (JWT will replace this later)
	@Column(nullable = false)
	private String username;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> items = new ArrayList<>();

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal totalAmount = BigDecimal.ZERO;

	@Column(nullable = false)
	private boolean checkedOut = false;

	private Instant createdAt;
	private Instant updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = Instant.now();
		this.updatedAt = this.createdAt;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = Instant.now();
	}

	/*
	 * ============================ Business Logic ============================
	 */
	public void recalculateTotal() {
		this.totalAmount = items.stream().map(CartItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/*
	 * ============================ Getters & Setters ============================
	 */
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public boolean isCheckedOut() {
		return checkedOut;
	}

	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
}
