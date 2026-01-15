package com.ey.dto.request;

import jakarta.validation.constraints.Size;

public class BrandUpdateRequest {

	@Size(max = 2)
	private String country;

	private Boolean active;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
