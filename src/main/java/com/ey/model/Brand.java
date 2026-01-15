package com.ey.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "brands", uniqueConstraints = {
		@UniqueConstraint(name = "uk_brand_name", columnNames = "name") }, indexes = {
				@Index(name = "idx_brand_active", columnList = "active") })
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Brand name cannot be blank")
	@Size(max = 100, message = "Brand name cannot exceed 100 characters")
	@Column(nullable = false, length = 100)
	private String name;

	@Size(max = 2, message = "Country must be ISO-2 code")
	@Column(length = 2)
	private String country;

	@Column(nullable = false)
	private boolean active = true;

	// ---- Soft delete fields ----
	private Instant deletedAt;

	@Column(length = 100)
	private String deletedBy;

	// ---- Audit fields ----
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	private Instant updatedAt;

	/*
	 * ========================== Lifecycle Callbacks ==========================
	 */

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
	 * ========================== Getters and Setters ==========================
	 */

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
}
