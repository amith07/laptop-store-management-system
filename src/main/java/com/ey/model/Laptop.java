package com.ey.model;

import java.math.BigDecimal;
import java.time.Instant;

import com.ey.enums.LaptopStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "laptops", uniqueConstraints = {
		@UniqueConstraint(name = "uk_laptop_serial", columnNames = "serialNumber") }, indexes = {
				@Index(name = "idx_laptop_status", columnList = "status"),
				@Index(name = "idx_laptop_brand", columnList = "brand_id") })
public class Laptop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * ========================== Relationships ==========================
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brand;

	/*
	 * ========================== Specifications ==========================
	 */
	@NotBlank
	private String modelName;

	@NotBlank
	@Column(nullable = false, unique = true)
	private String serialNumber;

	@NotBlank
	private String cpu;

	@Min(1)
	private int ramGb;

	@Min(1)
	private int storageGb;

	@DecimalMin("0.1")
	private BigDecimal screenSizeInches;

	/*
	 * ========================== Pricing ==========================
	 */
	@DecimalMin("0.01")
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal price;

	@DecimalMin("0.0")
	@DecimalMax("100.0")
	private BigDecimal discountPercentage;

	@DecimalMin("0.01")
	private BigDecimal promotionalPrice;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal effectivePrice;

	/*
	 * ========================== Inventory ==========================
	 */
	@Min(0)
	private int stock;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LaptopStatus status;

	/*
	 * ========================== Soft Delete & Audit ==========================
	 */
	private Instant deletedAt;

	private Instant createdAt;
	private Instant updatedAt;

	/*
	 * ========================== Lifecycle Hooks ==========================
	 */
	@PrePersist
	protected void onCreate() {
		this.createdAt = Instant.now();
		this.updatedAt = this.createdAt;
		computeEffectivePrice();
		updateStatus();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = Instant.now();
		computeEffectivePrice();
		updateStatus();
	}

	/*
	 * ========================== Business Logic ==========================
	 */
	public void computeEffectivePrice() {
		if (promotionalPrice != null) {
			this.effectivePrice = promotionalPrice;
		} else if (discountPercentage != null) {
			BigDecimal discount = price.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
			this.effectivePrice = price.subtract(discount);
		} else {
			this.effectivePrice = price;
		}
	}

	public void updateStatus() {
		this.status = (stock > 0) ? LaptopStatus.AVAILABLE : LaptopStatus.OUT_OF_STOCK;
	}

	/*
	 * ========================== Getters & Setters ==========================
	 */
	public Long getId() {
		return id;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public int getRamGb() {
		return ramGb;
	}

	public void setRamGb(int ramGb) {
		this.ramGb = ramGb;
	}

	public int getStorageGb() {
		return storageGb;
	}

	public void setStorageGb(int storageGb) {
		this.storageGb = storageGb;
	}

	public BigDecimal getScreenSizeInches() {
		return screenSizeInches;
	}

	public void setScreenSizeInches(BigDecimal screenSizeInches) {
		this.screenSizeInches = screenSizeInches;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public BigDecimal getPromotionalPrice() {
		return promotionalPrice;
	}

	public void setPromotionalPrice(BigDecimal promotionalPrice) {
		this.promotionalPrice = promotionalPrice;
	}

	public BigDecimal getEffectivePrice() {
		return effectivePrice;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public LaptopStatus getStatus() {
		return status;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
