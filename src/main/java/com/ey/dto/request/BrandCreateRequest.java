package com.ey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BrandCreateRequest {

	@NotBlank(message = "Brand name is required")
	@Size(max = 100)
	private String name;

	@Size(max = 2)
	private String country;

	private boolean active = true;

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
}
