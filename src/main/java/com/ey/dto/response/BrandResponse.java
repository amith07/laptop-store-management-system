package com.ey.dto.response;

public class BrandResponse {

	private Long id;
	private String name;
	private String country;
	private boolean active;

	public BrandResponse(Long id, String name, String country, boolean active) {
		this.id = id;
		this.name = name;
		this.country = country;
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	public boolean isActive() {
		return active;
	}
}
