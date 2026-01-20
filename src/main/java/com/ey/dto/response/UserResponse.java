package com.ey.dto.response;

import java.util.Set;
import java.util.stream.Collectors;

import com.ey.model.User;

public class UserResponse {

	private Long id;
	private String username;
	private String email;
	private String status;
	private Set<String> roles;

	public UserResponse(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.status = user.getStatus().name();
		this.roles = user.getRoles()
				.stream()
				.map(r -> r.getName())
				.collect(Collectors.toSet());
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getStatus() {
		return status;
	}

	public Set<String> getRoles() {
		return roles;
	}
}
